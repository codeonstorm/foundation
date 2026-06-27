# Phase 1: Complexity Analysis In Java

Welcome to Phase 1 of your DSA course. This phase teaches you how to judge the efficiency of code before you run it.

Complexity analysis is one of the most important DSA skills because interviews and real projects often ask the same question:

```text
Will this solution still work when the input becomes large?
```

## What You Will Learn

- What time complexity means
- What space complexity means
- How to use Big O notation
- How to analyze Java loops
- How to analyze nested loops
- How to analyze recursion
- How to compare brute force and optimized solutions
- How common Java collections affect complexity
- How to explain complexity clearly in interviews

## Folder Structure

Study these files in order:

| File | Topic |
| --- | --- |
| `01-what-is-complexity.md` | Meaning of complexity and input size |
| `02-big-o-rules.md` | Big O rules and simplification |
| `03-common-complexities.md` | Common time complexities with Java examples |
| `04-analyzing-loops.md` | Loop, nested loop, and pointer analysis |
| `05-recursion-complexity.md` | Recursive complexity and recurrence thinking |
| `06-space-complexity.md` | Space complexity and recursion stack |
| `07-java-collection-complexity.md` | Java collection operation costs |
| `08-comparing-solutions.md` | Brute force vs optimized solution comparison |
| `Practice.md` | Problems to solve yourself |
| `Solutions.md` | Explanations for the practice problems |
| `CheatSheet.md` | Fast revision notes |

Runnable Java examples are in:

```text
course/Phase1/examples/ComplexityExamples.java
```

## How To Run The Java Examples

From the repository root:

```powershell
javac course\Phase1\examples\ComplexityExamples.java
java -cp course\Phase1\examples ComplexityExamples
```

## Study Plan

Recommended pace: 3 to 5 days.

| Day | Study |
| --- | --- |
| Day 1 | Lessons 1 and 2 |
| Day 2 | Lessons 3 and 4 |
| Day 3 | Lessons 5 and 6 |
| Day 4 | Java collections and practice |
| Day 5 | Re-solve practice, revise cheat sheet |

## Learning Goal

By the end of this phase, you should be able to look at a Java function and say:

- What is the input size?
- How many times does the main work happen?
- Is the solution constant, logarithmic, linear, quadratic, or worse?
- How much extra memory does it use?
- Can it be improved?

## Phase 1 Coverage Checklist

- [x] Time complexity
- [x] Space complexity
- [x] Big O notation
- [x] Best, average, and worst case
- [x] Constant time: `O(1)`
- [x] Logarithmic time: `O(log n)`
- [x] Linear time: `O(n)`
- [x] Linearithmic time: `O(n log n)`
- [x] Quadratic time: `O(n^2)`
- [x] Exponential time: `O(2^n)`
- [x] Recursive complexity
- [x] Analyze loops
- [x] Analyze nested loops
- [x] Analyze recursion
- [x] Compare brute force and optimized solutions

## Interview Answer Template

Use this structure when explaining complexity:

```text
Let n be the size of the input.
The main loop runs n times.
Each iteration does O(1) work.
So the time complexity is O(n).
The algorithm uses only a few variables, so the space complexity is O(1).
```
