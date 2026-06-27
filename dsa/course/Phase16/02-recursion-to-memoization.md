# 02. Recursion To Memoization

Memoization means storing answers to recursive subproblems so they are not solved again.

The usual path is:

```text
Plain recursion -> notice repeated calls -> add cache -> memoized recursion
```

## Example: Fibonacci

Plain recursion:

```java
int fib(int n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

Problem:

```text
fib(3), fib(2), and many others are recomputed many times.
```

Time complexity: `O(2^n)`

## Add Memoization

Use an array where:

```text
memo[i] = fib(i)
```

Java:

```java
int fibMemo(int n) {
    int[] memo = new int[n + 1];
    Arrays.fill(memo, -1);
    return fibMemo(n, memo);
}

int fibMemo(int n, int[] memo) {
    if (n <= 1) {
        return n;
    }

    if (memo[n] != -1) {
        return memo[n];
    }

    memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    return memo[n];
}
```

Time complexity: `O(n)`

Space complexity: `O(n)`

Why?

Each value from `0` to `n` is computed once.

## Memoization Pattern

```java
returnType solve(state, memo) {
    if (baseCase) {
        return baseAnswer;
    }

    if (memo already has answer for state) {
        return memo value;
    }

    answer = calculate using recursive calls;
    memo[state] = answer;
    return answer;
}
```

## Example: Frog Jump

Problem:

```text
A frog is at index 0 and wants to reach index n - 1.
It can jump 1 or 2 steps.
Cost of jumping from i to j is abs(height[i] - height[j]).
Find minimum cost.
```

State:

```text
dp[i] = minimum cost to reach index i
```

Top-down version:

```java
int frogJumpMemo(int[] heights) {
    int n = heights.length;
    int[] memo = new int[n];
    Arrays.fill(memo, -1);
    return frogJumpMemo(n - 1, heights, memo);
}

int frogJumpMemo(int index, int[] heights, int[] memo) {
    if (index == 0) {
        return 0;
    }

    if (memo[index] != -1) {
        return memo[index];
    }

    int oneStep = frogJumpMemo(index - 1, heights, memo)
            + Math.abs(heights[index] - heights[index - 1]);

    int twoStep = Integer.MAX_VALUE;

    if (index > 1) {
        twoStep = frogJumpMemo(index - 2, heights, memo)
                + Math.abs(heights[index] - heights[index - 2]);
    }

    memo[index] = Math.min(oneStep, twoStep);
    return memo[index];
}
```

## Example: 0/1 Knapsack Memoization

Problem:

```text
You have weights and values.
Each item can be picked at most once.
Maximize value with capacity W.
```

State:

```text
dp[index][capacity] = maximum value using items from 0..index with remaining capacity
```

Java:

```java
int knapsackMemo(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] memo = new int[n][capacity + 1];

    for (int[] row : memo) {
        Arrays.fill(row, -1);
    }

    return knapsackMemo(n - 1, capacity, weights, values, memo);
}

int knapsackMemo(int index, int capacity, int[] weights, int[] values, int[][] memo) {
    if (index == 0) {
        if (weights[0] <= capacity) {
            return values[0];
        }
        return 0;
    }

    if (memo[index][capacity] != -1) {
        return memo[index][capacity];
    }

    int notTake = knapsackMemo(index - 1, capacity, weights, values, memo);

    int take = Integer.MIN_VALUE;
    if (weights[index] <= capacity) {
        take = values[index] + knapsackMemo(index - 1, capacity - weights[index], weights, values, memo);
    }

    memo[index][capacity] = Math.max(take, notTake);
    return memo[index][capacity];
}
```

Time complexity: `O(n * capacity)`

Space complexity: `O(n * capacity)` plus recursion stack.

## When Memoization Is Helpful

Use memoization when:

- Recursive solution is easy to write.
- Same states repeat.
- State space is limited.
- You are unsure about tabulation order.

## Memoization Checklist

- Define the state.
- Create a cache with one dimension per state variable.
- Initialize cache with a value that means unknown.
- Check cache before recursive work.
- Store answer before returning.

