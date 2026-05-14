#!/bin/bash

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Chipyard root is two levels up from sims/verilator
CHIPYARD_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# Check if simulator path is provided as argument
if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <simulator_exe_path>"
  echo "Example: $0 /path/to/simulator-chipyard.harness-MediumBoomV3SimConfig"
  exit 1
fi

SIMULATOR="$1"
BENCHMARKS_DIR="${CHIPYARD_ROOT}/toolchains/riscv-tools/riscv-tests/benchmarks"

# Check if simulator exists
if [ ! -f "$SIMULATOR" ]; then
  echo "Error: Simulator not found at $SIMULATOR"
  exit 1
fi

# Get all .riscv files
riscv_files=($(ls -1 "$BENCHMARKS_DIR"/*.riscv 2>/dev/null))

if [ ${#riscv_files[@]} -eq 0 ]; then
  echo "No .riscv files found in $BENCHMARKS_DIR"
  exit 1
fi

# Output all program names
echo "======================================"
echo "Found $(( ${#riscv_files[@]} )) RISCV programs:"
echo "======================================"
for file in "${riscv_files[@]}"; do
  echo "  - $(basename "$file")"
done
echo "======================================"
echo ""

# Execute run_boom.sh for each program
for file in "${riscv_files[@]}"; do
  prog_name="$(basename "$file")"
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] Running: $prog_name"
  ./run_boom.sh "$SIMULATOR" "$file"
  echo ""
done

echo "======================================"
echo "All benchmarks completed!"
echo "======================================"
