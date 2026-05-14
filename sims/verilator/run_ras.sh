#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEST_PROG="${1:-../../tests/call_return_prediction.riscv}"

# Track failed configs
failed_configs=()

# Find all MediumBoomV3SimConfig_* simulator binaries in the current directory
for sim in "$SCRIPT_DIR"/*RAS*; do
  # Skip if not an executable file
  [ -f "$sim" ] && [ -x "$sim" ] || continue

  sim_name="$(basename "$sim")"
  echo "========================================"
  echo "Running: $sim_name"
  echo "========================================"
  "$SCRIPT_DIR/run_boom.sh" "$sim_name" "$TEST_PROG"
  exit_code=$?
  echo "Exit code: $exit_code"
  echo ""
  
  # Track failed configs
  if [ $exit_code -ne 0 ]; then
    failed_configs+=("$sim_name")
  fi
done

# Print summary
echo "========================================"
echo "SUMMARY"
echo "========================================"
if [ ${#failed_configs[@]} -eq 0 ]; then
  echo "All configs passed!"
else
  echo "Failed configs (${#failed_configs[@]}):"
  for config in "${failed_configs[@]}"; do
    echo "  - $config"
  done
  exit 1
fi
