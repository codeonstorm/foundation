# Phase 6: Recursion And Backtracking In Java

Welcome to Phase 6 of your DSA course. This phase builds strong control over recursive thinking and then uses that control to solve backtracking problems.

Recursion and backtracking are less about memorizing code and more about learning to ask:

```text
What decision am I making now?
What smaller problem remains after this decision?
When should I stop?
How do I undo my decision before trying another choice?
```

## What You Will Learn

- Base case and recursive case
- How the Java call stack works
- How to draw recursion trees
- Tail recursion and why Java does not optimize it automatically
- Recursion with numbers, arrays, and strings
- The backtracking template: choose, explore, unchoose
- Subsets, subsequences, and permutations
- Combination sum
- Generate parentheses
- Letter combinations of a phone number
- N-Queens
- Sudoku solver
- Rat in a maze
- Word search
- Palindrome partitioning
- Restore IP addresses

## Folder Structure

Study these files in order:

| File | Topic |
| --- | --- |
| `01-recursion-basics.md` | Base case, recursive case, and call stack |
| `02-recursion-with-data.md` | Recursion with arrays and strings |
| `03-recursion-tree-and-complexity.md` | Recursion trees and complexity |
| `04-backtracking-template.md` | Choose, explore, unchoose |
| `05-subsets-subsequences-permutations.md` | Core generation patterns |
| `06-combination-sum-and-parentheses.md` | Combination sum and valid parentheses |
| `07-board-and-grid-backtracking.md` | N-Queens, Sudoku, maze, and word search |
| `08-string-backtracking.md` | Letter combinations, palindrome partitioning, IP addresses |
| `Practice.md` | Problems to solve yourself |
| `Solutions.md` | Guided solutions and explanations |
| `CheatSheet.md` | Fast revision notes |

Runnable Java examples are in:

```text
course/Phase6/examples/RecursionExamples.java
course/Phase6/examples/BacktrackingExamples.java
```

## How To Run The Java Examples

From the repository root:

```powershell
javac course\Phase6\examples\RecursionExamples.java
java -cp course\Phase6\examples RecursionExamples
```

```powershell
javac course\Phase6\examples\BacktrackingExamples.java
java -cp course\Phase6\examples BacktrackingExamples
```

## Recommended Study Plan

| Day | Study |
| --- | --- |
| Day 1 | Recursion basics, call stack, arrays, strings |
| Day 2 | Recursion trees and complexity |
| Day 3 | Backtracking template, subsets, subsequences |
| Day 4 | Permutations, combination sum, parentheses |
| Day 5 | N-Queens, Sudoku, maze, word search |
| Day 6 | String backtracking patterns |
| Day 7 | Practice, revise, and re-solve without looking |

## Phase 6 Coverage Checklist

- [x] Base case
- [x] Recursive case
- [x] Recursion tree
- [x] Call stack
- [x] Tail recursion
- [x] Recursion with arrays
- [x] Recursion with strings
- [x] Choose, explore, unchoose
- [x] Subsets
- [x] Subsequences
- [x] Permutations
- [x] Combination sum
- [x] N-Queens
- [x] Sudoku solver
- [x] Rat in a maze
- [x] Generate parentheses
- [x] Letter combinations of phone number
- [x] Word search
- [x] Palindrome partitioning
- [x] Restore IP addresses

## Learning Goal

By the end of this phase, you should be able to:

- Write a correct base case before the recursive call.
- Explain what each recursive call means.
- Draw a recursion tree before coding.
- Use backtracking to generate valid answers.
- Undo state changes correctly.
- Explain time and space complexity for recursive solutions.

## Core Mental Model

For recursion:

```text
Solve the current small piece.
Ask recursion to solve the smaller remaining problem.
```

For backtracking:

```text
Choose one option.
Explore all results from that option.
Undo the option.
Try the next option.
```

