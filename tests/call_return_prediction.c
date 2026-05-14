// #include "marchid.h"
#include "perf_define.h"
// #include <riscv-pk/encoding.h>
#include <stdint.h>
#include <stdio.h>

uint64_t counters[64];
uint64_t ptag = 0x1234567;

#define NOINLINE __attribute__((noinline))

// LCG pseudo-random number generator (used by pollution tests)
static uint64_t lcg_state = 1;
static inline uint64_t lcg_next() {
  lcg_state = lcg_state * 6364136223846793005ULL + 1ULL;
  return lcg_state;
}

// ============================================================
// 1) 深层调用链（测试 RAS 基本功能）
NOINLINE int leaf(int x) { return x + 1; }
NOINLINE int mid2(int x) { return leaf(x) + 1; }
NOINLINE int mid1(int x) { return mid2(x) + leaf(1); }
NOINLINE int bench_call_chain(int N) {
  volatile int acc = 0;
  for (int i = 0; i < N; ++i) {
    // 使得 leaf, mid2 函数的 ret 指令有多种返回地址
    acc += mid1(acc) + mid2(acc) + leaf(acc);
  }
  return acc;
}

// 7) 递归（防尾调用优化，形成真实返回栈弹栈）
NOINLINE int bench_recursion_inner(volatile int depth) {
  if (depth <= 0)
    return 1;
  return 1 + bench_recursion_inner(depth - 1);
}
NOINLINE int bench_recursion(int trips, int depth) {
  volatile int acc = 0;
  for (int i = 0; i < trips; ++i)
    acc += bench_recursion_inner(depth);
  return acc;
}

// ============================================================
// 2) 多次递归同一函数（测试 RCC 折叠）
// 同一函数反复递归调用，返回地址相同，应被 RCC 计数器折叠
NOINLINE int recursive_same_fn(volatile int depth) {
  if (depth <= 0)
    return 1;
  return 1 + recursive_same_fn(depth - 1);
}
NOINLINE int bench_multi_recursion(int trips, int depth) {
  volatile int acc = 0;
  for (int i = 0; i < trips; ++i) {
    // 每轮多次调用同一递归函数，每次深度不同
    // depth=10: 可被 4-bit RCC 完全折叠 (max 15)
    // depth=20: 超过 RCC 上限，需要分配 push
    // depth=depth: 用户指定深度
    acc += recursive_same_fn(10);
    acc += recursive_same_fn(20);
    acc += recursive_same_fn(depth);
  }
  return acc;
}

// ============================================================
// 3) RAS Pollution Test
//
// Mechanism: volatile memory read → unpredictable branch (~50% mispredict)
// → speculative ret-ret-call sequence corrupts RAS state.
//
// Structure per pollution chain:
//   polluter(flag) → ret → caller ret → call pollution_target (push wrong addr)
// ============================================================

// Leaf functions for building call chains and as pollution victims
NOINLINE int ras_leaf(int x) { return x + 1; }
NOINLINE int ras_mid(int x) { return ras_leaf(x) + 1; }
NOINLINE int ras_deep(int x) { return ras_mid(x) + 1; }

// Called speculatively after returns — the push pollutes RAS
NOINLINE int pollution_target(int x) { return x * 2; }

// Polluter: volatile memory read makes branch unpredictable
NOINLINE int ras_polluter_computed(int x, volatile int *flag) {
  int f = *flag;
  if (f) return x + 1;
  return x + 2;
}

// Chain: outer → inner → polluter → ret → call pollution_target (pollution point)
NOINLINE int computed_pollution_inner(int x, volatile int *flag) {
  int a = ras_polluter_computed(x, flag);
  int b = pollution_target(a);
  return b;
}

NOINLINE int computed_pollution_outer(int x, volatile int *flag) {
  int a = computed_pollution_inner(x, flag);
  int b = pollution_target(a);
  return b;
}

// bench_ras_pollution: single pollution chain per iteration
NOINLINE int bench_ras_pollution(int N) {
  volatile int acc = 0;
  volatile int flag = 0;
  
  for (int i = 0; i < N; ++i) {
    // Set up clean RAS state
    acc += ras_deep(acc);
    
    // flag alternates unpredictably based on LCG
    uint64_t rand_val = lcg_next();
    flag = (rand_val & 0x100) ? 1 : 0;
    
    // Trigger pollution - the memory load of 'flag' is unpredictable
    // causing branch misprediction and speculative ret-ret-call
    acc += computed_pollution_outer(acc, &flag);
    
    // These should suffer from RAS pollution
    acc += ras_mid(acc);
    acc += ras_leaf(acc);
  }
  
  return acc;
}

// ============================================================
// 4) Deep RAS Pollution — 3 distinct pollution chains (different PCs)
//    to prevent BTB from learning a single call/ret pattern
// ============================================================

// Chain D: deep_polluter_mem → deep_chain_inner → deep_chain_outer
NOINLINE int deep_polluter_mem(int x, volatile int *flag) {
  int f = *flag;
  if (f) return x + 1;
  return x + 2;
}

NOINLINE int deep_chain_inner(int x, volatile int *flag) {
  int a = deep_polluter_mem(x, flag);
  int b = pollution_target(a);
  return b;
}

NOINLINE int deep_chain_outer(int x, volatile int *flag) {
  int a = deep_chain_inner(x, flag);
  int b = pollution_target(a);
  return b;
}

// Chain A: polluter_A → chain_inner_A → chain_outer_A
NOINLINE int polluter_A(int x, volatile int *flag) {
  int f = *flag;
  if (f) return x + 1;
  return x + 2;
}

NOINLINE int chain_inner_A(int x, volatile int *flag) {
  int a = polluter_A(x, flag);
  int b = pollution_target(a);
  return b;
}

NOINLINE int chain_outer_A(int x, volatile int *flag) {
  int a = chain_inner_A(x, flag);
  int b = pollution_target(a);
  return b;
}

// Chain B: polluter_B → chain_inner_B → chain_outer_B
NOINLINE int polluter_B(int x, volatile int *flag) {
  int f = *flag;
  if (f) return x + 3;
  return x + 4;
}

NOINLINE int chain_inner_B(int x, volatile int *flag) {
  int a = polluter_B(x, flag);
  int b = pollution_target(a);
  return b;
}

NOINLINE int chain_outer_B(int x, volatile int *flag) {
  int a = chain_inner_B(x, flag);
  int b = pollution_target(a);
  return b;
}

NOINLINE int bench_deep_ras_pollution(int N) {
  volatile int acc = 0;
  volatile int flag = 0;
  
  for (int i = 0; i < N; ++i) {
    // Set up some RAS state first
    acc += ras_deep(acc);
    
    // 1st pollution (chain D) + victims
    uint64_t rand_val = lcg_next();
    flag = (rand_val & 0x100) ? 1 : 0;
    acc += deep_chain_outer(acc, &flag);
    acc += ras_mid(acc);
    acc += ras_leaf(acc);
    
    // 2nd pollution (chain A, different PCs) + victims
    rand_val = lcg_next();
    flag = (rand_val & 0x100) ? 1 : 0;
    acc += chain_outer_A(acc, &flag);
    acc += ras_mid(acc);
    acc += ras_leaf(acc);
    
    // 3rd pollution (chain B, different PCs) + victims
    rand_val = lcg_next();
    flag = (rand_val & 0x100) ? 1 : 0;
    acc += chain_outer_B(acc, &flag);
    acc += ras_mid(acc);
    acc += ras_leaf(acc);
  }
  
  return acc;
}

int main(void) {
  RESET_COUNTER();

  // 启用性能计数器
  SetPfcEnable(1);
  SetProcTag(ptag);
  SetCounterLevel("3");

  int N = 1000;

  // 测试 RAS
  bench_call_chain(N);
  // 40 层的递归调用会超过 medium boom 中 32 大小的
  // RAS，产生少量返回地址预测错误
  bench_recursion(N / 40, 40);

  // ReadCounter16(&counters[0], 0);
  // ReadCounter16(&counters[16], 16);


  // printf("[RAS] Committed Ret: %ld, Mispredicted Ret: %ld(mispredicted number "
  //        "should be small)\n",
  //        com_ret, misp_ret);
  // printf("[RAS] inst from f1 pred: %ld, from f2 pred: %ld, from f3 pred: %ld, "
  //        "from bsrcc: %ld(many from f3 because of ret?)\n",
  //        inst_from_f1, inst_from_f2, inst_from_f3, inst_from_bsrcc);

  // 测试多次递归同一函数
  // RESET_COUNTER();
  bench_multi_recursion(N / 10, 10);

  ReadCounter16(&counters[0], 0);
  ReadCounter16(&counters[16], 16);

  uint64_t com_br = counters[8];
  uint64_t com_ret = counters[10];
  uint64_t misp_br = counters[13];
  uint64_t misp_ret = counters[15];
  uint64_t inst_from_f1 = counters[16];
  uint64_t inst_from_f2 = counters[17];
  uint64_t inst_from_f3 = counters[18];
  uint64_t inst_from_bsrcc = counters[19];
  uint64_t ret_misp_rate_milli =
      (com_ret == 0) ? 0 : (misp_ret * 100000ULL + com_ret / 2) / com_ret;
  uint64_t br_misp_rate_milli =
      (com_br == 0) ? 0 : (misp_br * 100000ULL + com_br / 2) / com_br;
  printf("[Multi Recursion] Committed Ret: %ld, Mispredicted Ret: %ld, Mispredicted Rate: %ld.%03ld%%\n",
    com_ret, misp_ret, ret_misp_rate_milli / 1000,
    ret_misp_rate_milli % 1000);
  printf("[Multi Recursion] Committed BR: %ld, Mispredicted BR: %ld, Mispredicted Rate: %ld.%03ld%%\n",
    com_br, misp_br, br_misp_rate_milli / 1000,
    br_misp_rate_milli % 1000);
  printf("[Multi Recursion] inst from f1: %ld, f2: %ld, f3: %ld, bsrcc: %ld\n",
         inst_from_f1, inst_from_f2, inst_from_f3, inst_from_bsrcc);

  // 测试 RAS Pollution
  RESET_COUNTER();
  bench_ras_pollution(N);

  ReadCounter16(&counters[0], 0);
  ReadCounter16(&counters[16], 16);

  com_br = counters[8];
  com_ret = counters[10];
  misp_br = counters[13];
  misp_ret = counters[15];
  inst_from_f1 = counters[16];
  inst_from_f2 = counters[17];
  inst_from_f3 = counters[18];
  inst_from_bsrcc = counters[19];
  ret_misp_rate_milli =
      (com_ret == 0) ? 0 : (misp_ret * 100000ULL + com_ret / 2) / com_ret;
  br_misp_rate_milli =
      (com_br == 0) ? 0 : (misp_br * 100000ULL + com_br / 2) / com_br;
  printf("[RAS Pollution] Committed Ret: %ld, Mispredicted Ret: %ld, Mispredicted Rate: %ld.%03ld%%\n",
    com_ret, misp_ret, ret_misp_rate_milli / 1000,
    ret_misp_rate_milli % 1000);
  printf("[RAS Pollution] Committed BR: %ld, Mispredicted BR: %ld, Mispredicted Rate: %ld.%03ld%%\n",
    com_br, misp_br, br_misp_rate_milli / 1000,
    br_misp_rate_milli % 1000);
  printf("[RAS Pollution] inst from f1: %ld, f2: %ld, f3: %ld, bsrcc: %ld\n",
         inst_from_f1, inst_from_f2, inst_from_f3, inst_from_bsrcc);

  // 测试 Deep RAS Pollution
  // RESET_COUNTER();
  // bench_deep_ras_pollution(N);

  // ReadCounter16(&counters[0], 0);
  // ReadCounter16(&counters[16], 16);

  // com_ret = counters[10];
  // misp_ret = counters[15];
  // com_br = counters[8];
  // misp_br = counters[13];
  // inst_from_f1 = counters[16];
  // inst_from_f2 = counters[17];
  // inst_from_f3 = counters[18];
  // inst_from_bsrcc = counters[19];
  // printf("[Deep RAS Pollution] Committed Ret: %ld, Mispredicted Ret: %ld "
  //        "(deep nesting + random early returns)\n",
  //        com_ret, misp_ret);
  // printf("[Deep RAS Pollution] Committed BR: %ld, Mispredicted BR: %ld\n",
  //        com_br, misp_br);
  // printf(
  //     "[Deep RAS Pollution] inst from f1: %ld, f2: %ld, f3: %ld, bsrcc: %ld\n",
  //     inst_from_f1, inst_from_f2, inst_from_f3, inst_from_bsrcc);

  return 0;
}
