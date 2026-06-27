# Phase 6 Practice

Try these before reading `Solutions.md`.

For each problem:

- Write the base case.
- Write the recursive state.
- Draw a small recursion tree.
- Write time and space complexity.

## Part A: Recursion Basics

### 1. Print Decreasing

Write a recursive Java function that prints:

```text
n n-1 n-2 ... 1
```

Example:

```text
n = 5 -> 5 4 3 2 1
```

### 2. Print Increasing

Write a recursive Java function that prints:

```text
1 2 3 ... n
```

Example:

```text
n = 5 -> 1 2 3 4 5
```

### 3. Factorial

Write:

```java
int factorial(int n)
```

### 4. Sum Of Array

Write:

```java
int sum(int[] arr, int index)
```

### 5. Check Sorted Array

Write:

```java
boolean isSorted(int[] arr, int index)
```

Return `true` if the array is sorted in non-decreasing order.

### 6. Recursive Linear Search

Write:

```java
int search(int[] arr, int index, int target)
```

Return the first index of `target`, or `-1` if missing.

### 7. Reverse String

Write:

```java
String reverse(String s, int index)
```

### 8. Palindrome String

Write:

```java
boolean isPalindrome(String s, int left, int right)
```

## Part B: Recursion Tree And Complexity

### 9. Analyze Fibonacci

Analyze:

```java
int fib(int n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

Give time and space complexity.

### 10. Analyze Subset Shape

Analyze:

```java
void solve(int index, int n) {
    if (index == n) {
        return;
    }

    solve(index + 1, n);
    solve(index + 1, n);
}
```

Give time and space complexity.

## Part C: Backtracking Core

### 11. Generate Subsets

Write:

```java
List<List<Integer>> subsets(int[] nums)
```

### 12. Generate Subsequences Of A String

Write:

```java
List<String> subsequences(String s)
```

### 13. Generate Permutations

Write:

```java
List<List<Integer>> permute(int[] nums)
```

Use a `boolean[] used` array.

### 14. Combination Sum

Write:

```java
List<List<Integer>> combinationSum(int[] candidates, int target)
```

Numbers may be reused.

### 15. Generate Parentheses

Write:

```java
List<String> generateParenthesis(int n)
```

## Part D: Board And Grid Backtracking

### 16. N-Queens

Write:

```java
List<List<String>> solveNQueens(int n)
```

### 17. Sudoku Solver

Write:

```java
boolean solveSudoku(char[][] board)
```

Use `'.'` for empty cells.

### 18. Rat In A Maze

Given `int[][] grid`, where `1` is open and `0` is blocked, return all paths from `(0, 0)` to `(n - 1, n - 1)`.

Allowed moves:

```text
D, L, R, U
```

Write:

```java
List<String> findPaths(int[][] grid)
```

### 19. Word Search

Write:

```java
boolean exist(char[][] board, String word)
```

Each cell can be used only once in one path.

## Part E: String Backtracking

### 20. Letter Combinations

Write:

```java
List<String> letterCombinations(String digits)
```

Use phone keypad mapping.

### 21. Palindrome Partitioning

Write:

```java
List<List<String>> partition(String s)
```

Every part must be a palindrome.

### 22. Restore IP Addresses

Write:

```java
List<String> restoreIpAddresses(String s)
```

Rules:

- Exactly 4 parts
- Each part is `0` to `255`
- No leading zeroes, except `"0"`

## Final Reflection

For any three problems above, write:

```text
State:
Choices:
Base case:
Invalid case:
Choose:
Explore:
Unchoose:
Time:
Space:
```

