# Phase 16 Practice

Try these before reading `Solutions.md`.

For every problem, write:

```text
State:
Transition:
Base case:
Answer:
Time:
Space:
```

## Part A: DP Foundations

### 1. Fibonacci

Write three versions:

- Plain recursion
- Memoization
- Tabulation with space optimization

Function:

```java
int fib(int n)
```

### 2. Climbing Stairs

You can climb 1 or 2 steps.

Write:

```java
int climbStairs(int n)
```

### 3. Frog Jump

The frog starts at index `0` and wants to reach index `n - 1`.
It can jump 1 or 2 steps.

Cost:

```text
abs(heights[i] - heights[j])
```

Write:

```java
int frogJump(int[] heights)
```

### 4. House Robber

You cannot rob adjacent houses.

Write:

```java
int rob(int[] nums)
```

## Part B: Grid DP

### 5. Unique Paths

Move only right or down in an `m x n` grid.

Write:

```java
int uniquePaths(int rows, int cols)
```

### 6. Minimum Path Sum

Move only right or down.

Write:

```java
int minPathSum(int[][] grid)
```

## Part C: Subsequence And Knapsack DP

### 7. Coin Change: Minimum Coins

Coins can be reused.

Write:

```java
int coinChange(int[] coins, int amount)
```

Return `-1` if impossible.

### 8. Coin Change: Count Ways

Coins can be reused.
Order does not matter.

Write:

```java
int coinChangeWays(int[] coins, int amount)
```

### 9. 0/1 Knapsack

Each item can be chosen at most once.

Write:

```java
int knapsack01(int[] weights, int[] values, int capacity)
```

### 10. Unbounded Knapsack

Each item can be chosen unlimited times.

Write:

```java
int unboundedKnapsack(int[] weights, int[] values, int capacity)
```

### 11. Partition Equal Subset Sum

Write:

```java
boolean canPartition(int[] nums)
```

### 12. Rod Cutting

Given `price[i]` as the price of rod length `i + 1`, find maximum profit for rod length `n`.

Write:

```java
int rodCutting(int[] price, int n)
```

## Part D: String DP

### 13. Longest Common Subsequence

Write:

```java
int longestCommonSubsequence(String text1, String text2)
```

### 14. Edit Distance

Allowed operations:

- Insert
- Delete
- Replace

Write:

```java
int minDistance(String word1, String word2)
```

### 15. Count Palindromic Substrings

Write:

```java
int countSubstrings(String s)
```

## Part E: Advanced Patterns

### 16. Longest Increasing Subsequence

Write the `O(n^2)` DP version:

```java
int lengthOfLIS(int[] nums)
```

### 17. Matrix Chain Multiplication

Given dimensions array `arr`, matrix `i` has dimension:

```text
arr[i - 1] x arr[i]
```

Write:

```java
int matrixChainMultiplication(int[] arr)
```

### 18. Burst Balloons

Write:

```java
int maxCoins(int[] nums)
```

Think about which balloon bursts last inside an interval.

### 19. Egg Dropping

Write:

```java
int eggDrop(int eggs, int floors)
```

### 20. DP On Trees

For House Robber on a binary tree, define the state in words.

Then write a method that returns:

```text
[take, skip]
```

### 21. Bitmask DP

Given cost matrix where `cost[person][task]` is assignment cost, assign one task to each person with minimum total cost.

Write:

```java
int minAssignmentCost(int[][] cost)
```

## Final Reflection

Pick five problems and fill this:

```text
Problem:
Why recursion repeats work:
State:
Choices:
Transition:
Base case:
Tabulation order:
Can space be optimized?
```

