# Phase 16 Dynamic Programming Cheat Sheet

## DP Checklist

```text
State:
Transition:
Base case:
Answer:
Order:
Time:
Space:
```

## When To Use DP

Use DP when:

- Recursion repeats the same states.
- The best answer can be built from smaller answers.
- You are counting ways, minimizing, maximizing, or checking possibility.
- The problem has choices at each index, target, capacity, or interval.

## Memoization Template

```java
int solve(state, int[] memo) {
    if (baseCase) {
        return baseAnswer;
    }

    if (memo[state] != unknown) {
        return memo[state];
    }

    int answer = calculateFromSmallerStates();
    memo[state] = answer;
    return answer;
}
```

## Tabulation Template

```java
dp[base] = baseAnswer;

for (state in validOrder) {
    dp[state] = transitionFromPreviousStates;
}

return dp[answerState];
```

## Common States

| Pattern | State |
| --- | --- |
| 1D DP | `dp[i]` |
| Grid DP | `dp[row][col]` |
| Two strings | `dp[i][j]` |
| Subsequence target | `dp[index][target]` |
| Knapsack | `dp[index][capacity]` |
| Interval DP | `dp[left][right]` |
| Tree DP | answer for subtree rooted at node |
| Bitmask DP | `dp[mask]` |

## 1D DP

Fibonacci:

```text
dp[i] = dp[i - 1] + dp[i - 2]
```

House robber:

```text
dp[i] = max(dp[i - 1], nums[i] + dp[i - 2])
```

Frog jump:

```text
dp[i] = min(
  dp[i - 1] + abs(h[i] - h[i - 1]),
  dp[i - 2] + abs(h[i] - h[i - 2])
)
```

## Grid DP

Unique paths:

```text
dp[row][col] = dp[row - 1][col] + dp[row][col - 1]
```

Minimum path sum:

```text
dp[row][col] = grid[row][col] + min(top, left)
```

## Knapsack Rules

0/1 knapsack:

```text
Each item once.
Capacity loop backward when using 1D DP.
```

Unbounded knapsack:

```text
Item can repeat.
Capacity loop forward when using 1D DP.
```

## String DP

LCS:

```text
if same:
  dp[i][j] = 1 + dp[i - 1][j - 1]
else:
  dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
```

Edit distance:

```text
if same:
  dp[i][j] = dp[i - 1][j - 1]
else:
  dp[i][j] = 1 + min(insert, delete, replace)
```

Palindrome interval:

```text
dp[left][right] depends on dp[left + 1][right - 1]
```

## Interval DP

Matrix chain multiplication:

```text
dp[i][j] = min over k:
dp[i][k] + dp[k + 1][j] + mergeCost
```

Burst balloons:

```text
Choose last balloon in interval.
```

## Space Optimization

Ask:

```text
Which previous states are needed?
```

If only previous two values are needed:

```text
Use variables.
```

If only previous row is needed:

```text
Use one or two arrays.
```

## Common Mistakes

- Not defining state in words
- Wrong base case
- Wrong table fill order
- Mixing up substring and subsequence
- Using forward capacity loop for 0/1 knapsack
- Using backward capacity loop for unbounded knapsack
- Forgetting impossible initialization
- Space optimizing too early

## Best DP Interview Sentence

```text
Let dp[i] mean the best answer for the prefix ending at index i.
At each index, we choose whether to take or skip the current element.
The transition compares those choices, and the final answer is dp[n - 1].
```

