// test_ras.c
void trigger_coroutine_swap() {
    // Uses x5 (t0) as the jump target (source), and x1 (ra) as the link (dest)
    // This satisfies both RAS conditions: POP (due to x5) and PUSH (due to x1)
    asm volatile("jalr x1, 0(x5)");
}

int main() {
    trigger_coroutine_swap();
    return 0;
}