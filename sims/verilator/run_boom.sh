#!/bin/bash

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Chipyard root is two levels up from sims/verilator
CHIPYARD_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# Require two arguments: simulator executable path and riscv program path
if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <simulator_exe_path> <riscv_prog_path> [--fst] [prog_args...]"
  exit 1
fi

SIM="$1"; shift
SIM_NAME="$(basename "$SIM")"
PROG="$1"; shift

# Check for --fst flag
ENABLE_FST=0
PROG_ARGS=()
for arg in "$@"; do
  if [ "$arg" = "--fst" ]; then
    ENABLE_FST=1
  else
    PROG_ARGS+=("$arg")
  fi
done

PROG_NAME="$(basename "$PROG")"
PROG_STEM="${PROG_NAME%.*}"
LOG_NAME_BASE="${SIM_NAME}_${PROG_STEM}"

# Build the simulator command as an array

# +ras-printf=1
# +prev-ras-update=1
# +ras-ctr-update=1   发生 ctr 更新时打印对应的栈顶内容
# +rvc-exp-result=1   RVC 拓展及译码内容
# +ras-content=1      打印 RAS 内容
# +debug-ret-frontend=1  打印前端 Return 指令PC、RAS 栈顶地址和偏移量
# +debug-ret-backend=1   打印后端 Return 指令PC、实际目标地址和偏移量

# +btb-reset-value=8 
# +btb-meta-reset-value=2097153
# +predecode-target-printf=1
# +enq-ghist-update=1
# +f2btb-access-printf=1
# +faubtb-pred-printf=1
# +f2btb-update-printf=1
# +f2btb-mis-update-printf=1

cmd=( "./$SIM"
  +permissive
  +dramsim +dramsim_ini_dir="${CHIPYARD_ROOT}/generators/testchipip/src/main/resources/dramsim2_ini"
  +max-cycles=100000000
  +loadmem="$PROG"
  +verbose
  # +ras-ctr-update=1
  # +rvc-exp-result=1
  # +ras-content=1
  # +ras-sq-content=1
  # +ras-rs-content=1
  # +debug-ret-frontend=1
  # +debug-ret-backend=1
  +verilator+seed+83540
)

# Add vcdfile argument only if --fst flag is passed
if [ "$ENABLE_FST" -eq 1 ]; then
  cmd+=( +vcdfile="${SCRIPT_DIR}/verilator_boom/${LOG_NAME_BASE}.fst" )
  echo "FST file: ${SCRIPT_DIR}/verilator_boom/${LOG_NAME_BASE}.fst"
fi

cmd+=(
  +permissive-off
  "$PROG" "${PROG_ARGS[@]}"
)

echo "Running command: ${cmd[*]}"
echo "log file: ${SCRIPT_DIR}/verilator_boom/${LOG_NAME_BASE}.log"
echo "output file: ${SCRIPT_DIR}/verilator_boom/${LOG_NAME_BASE}.out"

(set -o pipefail &&  "${cmd[@]}" \
        </dev/null 2> >(spike-dasm > "${SCRIPT_DIR}/verilator_boom/${LOG_NAME_BASE}.out") | tee "${SCRIPT_DIR}/verilator_boom/${LOG_NAME_BASE}.log")