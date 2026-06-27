# 03. Tabulation And Space Optimization

Tabulation is bottom-up DP.

Instead of recursion asking for smaller states, we fill smaller states first and build toward the final answer.

## Fibonacci: Memoization To Tabulation

Memoization:

```text
fib(n) asks for fib(n - 1) and fib(n - 2)
```

Tabulation:

```text
fill fib(0), fib(1), fib(2), ... fib(n)
```

Java:

```java
int fibTab(int n) {
    if (n <= 1) {
        return n;
    }

    int[] dp = new int[n + 1];
    dp[0] = 0;
    dp[1] = 1;

    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }

    return dp[n];
}
```

Time complexity: `O(n)`

Space complexity: `O(n)`

## Space Optimization

If the current state only needs the previous two states, we do not need the whole array.

```java
int fibSpaceOptimized(int n) {
    if (n <= 1) {
        return n;
    }

    int prev2 = 0;
    int prev1 = 1;

    for (int i = 2; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

Time complexity: `O(n)`

Space complexity: `O(1)`

## How To Convert Memoization To Tabulation

Use this process:

1. Write the memoized recurrence.
2. Identify base cases.
3. Create a DP table.
4. Fill base cases first.
5. Fill remaining states in an order where dependencies are already known.
6. Return the final state.

## Example: Climbing Stairs

State:

```text
dp[i] = number of ways to reach stair i
```

Transition:

```text
dp[i] = dp[i - 1] + dp[i - 2]
```

Java:

```java
int climbStairs(int n) {
    if (n <= 1) {
        return 1;
    }

    int[] dp = new int[n + 1];
    dp[0] = 1;
    dp[1] = 1;

    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }

    return dp[n];
}
```

Space optimized:

```java
int climbStairsOptimized(int n) {
    if (n <= 1) {
        return 1;
    }

    int prev2 = 1;
    int prev1 = 1;

    for (int i = 2; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

## 2D Tabulation

Example state:

```text
dp[row][col] = answer for cell row, col
```

For grid problems, you often fill:

- Top to bottom
- Left to right

Because each cell may depend on:

```text
top cell
left cell
```

## Space Optimization In 2D DP

If each row only depends on the previous row, you can keep:

```text
previous row
current row
```

Or sometimes just one row.

Example:

```java
int uniquePathsOptimized(int rows, int cols) {
    int[] dp = new int[cols];
    Arrays.fill(dp, 1);

    for (int row = 1; row < rows; row++) {
        for (int col = 1; col < cols; col++) {
            dp[col] = dp[col] + dp[col - 1];
        }
    }

    return dp[cols - 1];
}
```

Here:

- `dp[col]` before update means value from top.
- `dp[col - 1]` means value from left.

## Space Optimization Rule

Only optimize space after the full DP is correct.

Ask:

```text
Which previous states does the current state need?
```

If it only needs a few previous values, reduce memory.

If it needs many states, keep the table.

