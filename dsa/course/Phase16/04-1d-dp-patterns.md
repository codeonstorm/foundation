# 04. 1D DP Patterns

1D DP usually means one variable is enough to describe the subproblem.

Common state:

```text
dp[i] = answer for index i
```

## Fibonacci

State:

```text
dp[i] = ith Fibonacci number
```

Transition:

```text
dp[i] = dp[i - 1] + dp[i - 2]
```

Java:

```java
int fib(int n) {
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

Time: `O(n)`

Space: `O(1)`

## Climbing Stairs

Problem:

```text
You can climb 1 or 2 steps.
How many ways can you reach stair n?
```

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

## House Robber

Problem:

```text
Each house has money.
You cannot rob adjacent houses.
Find maximum money.
```

State:

```text
dp[i] = maximum money we can rob from houses 0..i
```

Choices:

- Do not rob house `i`: answer is `dp[i - 1]`
- Rob house `i`: answer is `nums[i] + dp[i - 2]`

Transition:

```text
dp[i] = max(dp[i - 1], nums[i] + dp[i - 2])
```

Java:

```java
int rob(int[] nums) {
    int prev2 = 0;
    int prev1 = 0;

    for (int money : nums) {
        int current = Math.max(prev1, money + prev2);
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

Time: `O(n)`

Space: `O(1)`

## Frog Jump

Problem:

```text
A frog starts at index 0 and wants to reach index n - 1.
It can jump 1 or 2 steps.
Cost is abs(heights[i] - heights[j]).
Find minimum cost.
```

State:

```text
dp[i] = minimum cost to reach index i
```

Transition:

```text
dp[i] = min(
    dp[i - 1] + abs(height[i] - height[i - 1]),
    dp[i - 2] + abs(height[i] - height[i - 2])
)
```

Java:

```java
int frogJump(int[] heights) {
    int n = heights.length;

    if (n <= 1) {
        return 0;
    }

    int[] dp = new int[n];
    dp[0] = 0;
    dp[1] = Math.abs(heights[1] - heights[0]);

    for (int i = 2; i < n; i++) {
        int oneStep = dp[i - 1] + Math.abs(heights[i] - heights[i - 1]);
        int twoStep = dp[i - 2] + Math.abs(heights[i] - heights[i - 2]);
        dp[i] = Math.min(oneStep, twoStep);
    }

    return dp[n - 1];
}
```

Space optimized:

```java
int frogJumpOptimized(int[] heights) {
    int n = heights.length;

    if (n <= 1) {
        return 0;
    }

    int prev2 = 0;
    int prev1 = Math.abs(heights[1] - heights[0]);

    for (int i = 2; i < n; i++) {
        int oneStep = prev1 + Math.abs(heights[i] - heights[i - 1]);
        int twoStep = prev2 + Math.abs(heights[i] - heights[i - 2]);
        int current = Math.min(oneStep, twoStep);

        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

## Min Cost Climbing Stairs

Problem:

```text
You can climb 1 or 2 steps.
Each stair has a cost when you step on it.
You can start at stair 0 or 1.
Find minimum cost to reach the top.
```

State:

```text
dp[i] = minimum cost to reach stair i
```

Java:

```java
int minCostClimbingStairs(int[] cost) {
    int prev2 = 0;
    int prev1 = 0;

    for (int i = 2; i <= cost.length; i++) {
        int current = Math.min(prev1 + cost[i - 1], prev2 + cost[i - 2]);
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

## How To Recognize 1D DP

Use 1D DP when:

- The problem moves left to right through an array.
- The answer at index `i` depends on earlier indices.
- You choose between taking or skipping something.
- You can describe the state with one index.

## Common 1D DP Transitions

| Problem | Transition |
| --- | --- |
| Fibonacci | `dp[i] = dp[i - 1] + dp[i - 2]` |
| Climbing stairs | `dp[i] = dp[i - 1] + dp[i - 2]` |
| House robber | `dp[i] = max(dp[i - 1], nums[i] + dp[i - 2])` |
| Frog jump | `dp[i] = min(oneStep, twoStep)` |
| LIS | `dp[i] = 1 + max(dp[j]) where arr[j] < arr[i]` |

