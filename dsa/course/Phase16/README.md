# Phase 16: Dynamic Programming In Java

Welcome to Phase 16 of your DSA course. This phase teaches you how to solve problems with overlapping subproblems and optimal substructure.

Dynamic Programming, or DP, is not a single algorithm. It is a way to turn repeated recursive work into stored answers.

The most important DP question is:

```text
What does dp[state] mean in plain English?
```

If you can define the state clearly, the transition usually becomes much easier.

## What You Will Learn

- Recursion to memoization
- Memoization to tabulation
- State definition
- Transition
- Base cases
- Space optimization
- 1D DP
- 2D DP
- DP on strings
- DP on subsequences
- DP on grids
- DP on trees
- DP with bitmasking
- Fibonacci, climbing stairs, house robber, frog jump
- Coin change, knapsack, partition equal subset sum, rod cutting
- Unique paths, minimum path sum
- Longest increasing subsequence
- Longest common subsequence
- Edit distance
- Palindromic substrings
- Matrix chain multiplication
- Burst balloons
- Egg dropping

## Folder Structure

Study these files in order:

| File | Topic |
| --- | --- |
| `01-dp-mindset.md` | DP intuition, state, transition, base case |
| `02-recursion-to-memoization.md` | Converting recursion into memoization |
| `03-tabulation-and-space-optimization.md` | Bottom-up DP and reducing memory |
| `04-1d-dp-patterns.md` | Fibonacci, stairs, house robber, frog jump |
| `05-grid-dp.md` | Unique paths and minimum path sum |
| `06-subsequence-and-knapsack-dp.md` | Coin change, 0/1 knapsack, unbounded knapsack, partition, rod cutting |
| `07-string-dp.md` | LCS, edit distance, palindromic substrings |
| `08-advanced-dp-patterns.md` | LIS, matrix chain multiplication, burst balloons, tree DP, bitmask DP, egg dropping |
| `Practice.md` | Problems to solve yourself |
| `Solutions.md` | Guided solutions and explanations |
| `CheatSheet.md` | Fast revision notes |

Runnable Java examples are in:

```text
course/Phase16/examples/DynamicProgrammingExamples.java
```

## How To Run The Java Examples

From the repository root:

```powershell
javac course\Phase16\examples\DynamicProgrammingExamples.java
java -cp course\Phase16\examples DynamicProgrammingExamples
```

## Recommended Study Plan

| Day | Study |
| --- | --- |
| Day 1 | DP mindset, state, transition, base cases |
| Day 2 | Recursion to memoization |
| Day 3 | Tabulation and space optimization |
| Day 4 | 1D DP patterns |
| Day 5 | Grid DP |
| Day 6 | Subsequence and knapsack DP |
| Day 7 | String DP |
| Day 8 | Advanced patterns |
| Day 9 | Practice and revise |
| Day 10 | Re-solve without looking |

## Phase 16 Coverage Checklist

- [x] Recursion to memoization
- [x] Memoization to tabulation
- [x] State definition
- [x] Transition
- [x] Base cases
- [x] Space optimization
- [x] 1D DP
- [x] 2D DP
- [x] DP on strings
- [x] DP on subsequences
- [x] DP on grids
- [x] DP on trees
- [x] DP with bitmasking
- [x] Fibonacci
- [x] Climbing stairs
- [x] House robber
- [x] Frog jump
- [x] Coin change
- [x] Minimum path sum
- [x] Unique paths
- [x] Longest increasing subsequence
- [x] Longest common subsequence
- [x] Edit distance
- [x] Palindromic substrings
- [x] Matrix chain multiplication
- [x] Burst balloons
- [x] 0/1 knapsack
- [x] Unbounded knapsack
- [x] Partition equal subset sum
- [x] Rod cutting
- [x] Egg dropping

## DP Learning Goal

By the end of this phase, you should be able to:

- Define a DP state in plain English.
- Write the recurrence or transition.
- Identify base cases.
- Decide between memoization and tabulation.
- Optimize space when only previous states are needed.
- Recognize common DP patterns.
- Explain time and space complexity clearly.

## Core Template

Use this thinking process:

```text
1. State: What does dp[...] represent?
2. Choice: What options do I have?
3. Transition: How do choices connect to smaller states?
4. Base case: What answers are already known?
5. Order: In what order should states be computed?
6. Answer: Which dp state gives the final result?
```

