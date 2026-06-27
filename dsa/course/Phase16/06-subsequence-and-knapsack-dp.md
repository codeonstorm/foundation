# 06. Subsequence And Knapsack DP

Subsequence DP usually asks whether to take or skip an item.

Common state:

```text
dp[index][target]
dp[index][capacity]
```

## Coin Change: Minimum Coins

Problem:

```text
Given coin values and amount, find minimum coins needed.
You may use each coin unlimited times.
```

State:

```text
dp[amount] = minimum coins needed to make amount
```

Transition:

```text
dp[a] = min(dp[a], 1 + dp[a - coin])
```

Java:

```java
int coinChange(int[] coins, int amount) {
    int impossible = amount + 1;
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, impossible);
    dp[0] = 0;

    for (int coin : coins) {
        for (int a = coin; a <= amount; a++) {
            dp[a] = Math.min(dp[a], 1 + dp[a - coin]);
        }
    }

    return dp[amount] == impossible ? -1 : dp[amount];
}
```

Time: `O(coins.length * amount)`

Space: `O(amount)`

## Coin Change: Count Ways

Problem:

```text
Count how many combinations make the amount.
Order does not matter.
```

State:

```text
dp[a] = number of ways to make amount a
```

Java:

```java
int coinChangeWays(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;

    for (int coin : coins) {
        for (int a = coin; a <= amount; a++) {
            dp[a] += dp[a - coin];
        }
    }

    return dp[amount];
}
```

Looping coins outside prevents counting different orders as different combinations.

## 0/1 Knapsack

Problem:

```text
Each item can be picked at most once.
Maximize value under weight capacity.
```

State:

```text
dp[i][w] = maximum value using first i items with capacity w
```

Transition:

```text
notTake = dp[i - 1][w]
take = value[i - 1] + dp[i - 1][w - weight[i - 1]]
dp[i][w] = max(take, notTake)
```

Java:

```java
int knapsack01(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n + 1][capacity + 1];

    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= capacity; w++) {
            int notTake = dp[i - 1][w];

            int take = 0;
            if (weights[i - 1] <= w) {
                take = values[i - 1] + dp[i - 1][w - weights[i - 1]];
            }

            dp[i][w] = Math.max(take, notTake);
        }
    }

    return dp[n][capacity];
}
```

Time: `O(n * capacity)`

Space: `O(n * capacity)`

## 0/1 Knapsack Space Optimized

For 0/1 knapsack, iterate capacity backward so one item is not reused in the same row.

```java
int knapsack01Optimized(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];

    for (int i = 0; i < weights.length; i++) {
        for (int w = capacity; w >= weights[i]; w--) {
            dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
        }
    }

    return dp[capacity];
}
```

Space: `O(capacity)`

## Unbounded Knapsack

Problem:

```text
Each item can be picked unlimited times.
```

For unbounded knapsack, iterate capacity forward.

```java
int unboundedKnapsack(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];

    for (int i = 0; i < weights.length; i++) {
        for (int w = weights[i]; w <= capacity; w++) {
            dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
        }
    }

    return dp[capacity];
}
```

Why forward?

Because the same item is allowed to contribute again.

## Partition Equal Subset Sum

Problem:

```text
Can the array be split into two subsets with equal sum?
```

If total sum is odd, answer is false.

Otherwise, check whether a subset with sum `total / 2` exists.

Java:

```java
boolean canPartition(int[] nums) {
    int total = 0;

    for (int num : nums) {
        total += num;
    }

    if (total % 2 != 0) {
        return false;
    }

    int target = total / 2;
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;

    for (int num : nums) {
        for (int sum = target; sum >= num; sum--) {
            dp[sum] = dp[sum] || dp[sum - num];
        }
    }

    return dp[target];
}
```

This is 0/1 subset sum.

## Rod Cutting

Problem:

```text
Given prices for rod lengths, cut a rod to maximize profit.
```

This is unbounded knapsack because each cut length can be used multiple times.

```java
int rodCutting(int[] price, int n) {
    int[] dp = new int[n + 1];

    for (int length = 1; length <= n; length++) {
        for (int cut = 1; cut <= length; cut++) {
            dp[length] = Math.max(dp[length], price[cut - 1] + dp[length - cut]);
        }
    }

    return dp[n];
}
```

## Take Or Skip Template

```java
int notTake = solve(index - 1, target);
int take = 0;

if (canTake) {
    take = value + solve(nextIndex, reducedTarget);
}

return best(take, notTake);
```

For 0/1 problems:

```text
nextIndex = index - 1
```

For unbounded problems:

```text
nextIndex = index
```

