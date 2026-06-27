# Phase 17: Bit Manipulation In Java

Welcome to Phase 17 of your DSA course. This phase teaches you how to use binary representation to solve problems faster and with less memory.

Bit manipulation can feel strange at first because you are no longer thinking in decimal numbers. The main shift is:

```text
An integer is a row of bits.
Each bit can be checked, changed, combined, or used as a flag.
```

## What You Will Learn

- Binary representation
- AND, OR, XOR, NOT
- Left shift and right shift
- Java `>>` vs `>>>`
- Set bit
- Clear bit
- Toggle bit
- Check bit
- Count set bits
- Power of two
- Bit masks
- Single number
- Missing number
- Counting bits
- Subsets using bitmask
- Maximum XOR
- Bitmask DP basics

## Folder Structure

Study these files in order:

| File | Topic |
| --- | --- |
| `01-binary-and-bitwise-operators.md` | Binary basics, AND, OR, XOR, NOT, shifts |
| `02-common-bit-operations.md` | Set, clear, toggle, check, masks |
| `03-xor-patterns.md` | Single number, missing number, XOR reasoning |
| `04-counting-bits-and-power-of-two.md` | Count set bits, Brian Kernighan, counting bits DP |
| `05-subsets-with-bitmask.md` | Generating subsets with masks |
| `06-maximum-xor-and-bitmask-dp.md` | Maximum XOR and bitmask DP basics |
| `Practice.md` | Problems to solve yourself |
| `Solutions.md` | Guided solutions and explanations |
| `CheatSheet.md` | Fast revision notes |

Runnable Java examples are in:

```text
course/Phase17/examples/BitManipulationExamples.java
```

## How To Run The Java Examples

From the repository root:

```powershell
javac course\Phase17\examples\BitManipulationExamples.java
java -cp course\Phase17\examples BitManipulationExamples
```

## Recommended Study Plan

| Day | Study |
| --- | --- |
| Day 1 | Binary basics and operators |
| Day 2 | Set, clear, toggle, check, masks |
| Day 3 | XOR patterns |
| Day 4 | Counting bits and power of two |
| Day 5 | Subsets using bitmask |
| Day 6 | Maximum XOR and bitmask DP |
| Day 7 | Practice and revision |

## Phase 17 Coverage Checklist

- [x] AND, OR, XOR
- [x] Left shift and right shift
- [x] Set bit
- [x] Clear bit
- [x] Toggle bit
- [x] Check bit
- [x] Count set bits
- [x] Power of two
- [x] Bit masks
- [x] Single number
- [x] Missing number
- [x] Counting bits
- [x] Subsets using bitmask
- [x] Maximum XOR
- [x] Bitmask DP basics

## Learning Goal

By the end of this phase, you should be able to:

- Explain what a bit mask represents.
- Use `&`, `|`, `^`, `~`, `<<`, `>>`, and `>>>` correctly.
- Set, clear, toggle, and check bits without guessing.
- Use XOR to cancel duplicate values.
- Count set bits efficiently.
- Generate all subsets using masks.
- Understand why bitmask DP has `2^n` states.

