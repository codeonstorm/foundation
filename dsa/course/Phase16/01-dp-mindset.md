# 01. DP Mindset

Dynamic Programming is used when a problem has:

- Overlapping subproblems
- Optimal substructure

## Overlapping Subproblems

A problem has overlapping subproblems when the same smaller problem is solved again and again.

Example:

```java
int fib(int n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

`fib(5)` calls `fib(4)` and `fib(3)`.
`fib(4)` calls `fib(3)` again.

The same `fib(3)` is solved more than once.

That repeated work is the signal for DP.

## Optimal Substructure

A problem has optimal substructure when the answer to a big problem can be built from answers to smaller problems.

Example:

```text
fib(n) = fib(n - 1) + fib(n - 2)
```

If you know `fib(n - 1)` and `fib(n - 2)`, you know `fib(n)`.

## The Most Important DP Skill

Before writing code, define the state.

State means:

```text
The information needed to describe one subproblem.
```

Examples:

| Problem | State |
| --- | --- |
| Fibonacci | `n` |
| Climbing stairs | current stair number |
| Grid paths | `row, col` |
| LCS | `i, j` positions in two strings |
| Knapsack | `index, capacity` |
| Matrix chain | `left, right` interval |
| Bitmask assignment | `mask` of used items |

## State Definition

A good state definition sounds like plain English.

Example:

```text
dp[i] = number of ways to reach stair i
```

Then the transition becomes natural:

```text
To reach stair i, I can come from i - 1 or i - 2.
dp[i] = dp[i - 1] + dp[i - 2]
```

## Transition

Transition means:

```text
How do I calculate the current state from smaller states?
```

Example:

```text
dp[i] = dp[i - 1] + dp[i - 2]
```

This is the transition for climbing stairs.

## Base Cases

Base cases are answers that are already known.

For climbing stairs:

```text
dp[0] = 1
dp[1] = 1
```

Meaning:

- There is one way to stand at stair `0`: do nothing.
- There is one way to reach stair `1`: take one step.

## Answer

After filling DP states, ask:

```text
Which state contains the final answer?
```

For climbing stairs:

```text
answer = dp[n]
```

For LCS:

```text
answer = dp[n][m]
```

For knapsack:

```text
answer = dp[n][capacity]
```

## Memoization Vs Tabulation

Two common ways to write DP:

| Style | Meaning |
| --- | --- |
| Memoization | Top-down recursion plus cache |
| Tabulation | Bottom-up table filling |

Memoization is often easier when the recurrence is natural.

Tabulation is often easier to optimize and avoids recursion stack issues.

## DP Problem Solving Checklist

Use this every time:

1. Can brute force recursion solve it?
2. Are subproblems repeated?
3. What is the state?
4. What are the choices?
5. What is the transition?
6. What are the base cases?
7. What is the final answer state?
8. Can space be optimized?

## Common Mistakes

- Starting with code before defining the state
- Using too many dimensions in the state
- Missing base cases
- Filling the table in the wrong order
- Confusing index meaning
- Forgetting to initialize impossible states
- Space optimizing before the full DP is understood

