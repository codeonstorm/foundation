# Phase 16 Practice Solutions

Use this after attempting `Practice.md`.

## 1. Fibonacci

State:

```text
dp[i] = ith Fibonacci number
```

Space optimized:

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

## 2. Climbing Stairs

State:

```text
dp[i] = number of ways to reach stair i
```

Transition:

```text
dp[i] = dp[i - 1] + dp[i - 2]
```

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

Time: `O(n)`

Space: `O(1)`

## 3. Frog Jump

State:

```text
dp[i] = minimum cost to reach index i
```

```java
int frogJump(int[] heights) {
    if (heights.length <= 1) {
        return 0;
    }

    int prev2 = 0;
    int prev1 = Math.abs(heights[1] - heights[0]);

    for (int i = 2; i < heights.length; i++) {
        int oneStep = prev1 + Math.abs(heights[i] - heights[i - 1]);
        int twoStep = prev2 + Math.abs(heights[i] - heights[i - 2]);
        int current = Math.min(oneStep, twoStep);

        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

Time: `O(n)`

Space: `O(1)`

## 4. House Robber

State:

```text
dp[i] = max money from houses 0..i
```

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

## 5. Unique Paths

State:

```text
dp[row][col] = number of ways to reach row, col
```

```java
int uniquePaths(int rows, int cols) {
    int[] dp = new int[cols];
    Arrays.fill(dp, 1);

    for (int row = 1; row < rows; row++) {
        for (int col = 1; col < cols; col++) {
            dp[col] += dp[col - 1];
        }
    }

    return dp[cols - 1];
}
```

Time: `O(rows * cols)`

Space: `O(cols)`

## 6. Minimum Path Sum

State:

```text
dp[col] = minimum path sum to current row, col
```

```java
int minPathSum(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    int[] dp = new int[cols];

    dp[0] = grid[0][0];

    for (int col = 1; col < cols; col++) {
        dp[col] = dp[col - 1] + grid[0][col];
    }

    for (int row = 1; row < rows; row++) {
        dp[0] += grid[row][0];

        for (int col = 1; col < cols; col++) {
            dp[col] = grid[row][col] + Math.min(dp[col], dp[col - 1]);
        }
    }

    return dp[cols - 1];
}
```

Time: `O(rows * cols)`

Space: `O(cols)`

## 7. Coin Change: Minimum Coins

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

## 8. Coin Change: Count Ways

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

Coins loop outside means combinations, not permutations.

## 9. 0/1 Knapsack

```java
int knapsack01(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];

    for (int i = 0; i < weights.length; i++) {
        for (int w = capacity; w >= weights[i]; w--) {
            dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
        }
    }

    return dp[capacity];
}
```

Backward capacity loop prevents reusing the same item.

Time: `O(n * capacity)`

Space: `O(capacity)`

## 10. Unbounded Knapsack

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

Forward capacity loop allows reusing the same item.

## 11. Partition Equal Subset Sum

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

This is subset sum with target `total / 2`.

## 12. Rod Cutting

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

This is unbounded knapsack.

## 13. Longest Common Subsequence

```java
int longestCommonSubsequence(String a, String b) {
    int n = a.length();
    int m = b.length();
    int[][] dp = new int[n + 1][m + 1];

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                dp[i][j] = 1 + dp[i - 1][j - 1];
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }

    return dp[n][m];
}
```

Time: `O(n * m)`

Space: `O(n * m)`

## 14. Edit Distance

```java
int minDistance(String word1, String word2) {
    int n = word1.length();
    int m = word2.length();
    int[][] dp = new int[n + 1][m + 1];

    for (int i = 0; i <= n; i++) {
        dp[i][0] = i;
    }

    for (int j = 0; j <= m; j++) {
        dp[0][j] = j;
    }

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                int insert = dp[i][j - 1];
                int delete = dp[i - 1][j];
                int replace = dp[i - 1][j - 1];
                dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
            }
        }
    }

    return dp[n][m];
}
```

## 15. Count Palindromic Substrings

```java
int countSubstrings(String s) {
    int count = 0;

    for (int center = 0; center < s.length(); center++) {
        count += expand(s, center, center);
        count += expand(s, center, center + 1);
    }

    return count;
}

int expand(String s, int left, int right) {
    int count = 0;

    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
        count++;
        left--;
        right++;
    }

    return count;
}
```

Time: `O(n^2)`

Space: `O(1)`

## 16. Longest Increasing Subsequence

```java
int lengthOfLIS(int[] nums) {
    int[] dp = new int[nums.length];
    Arrays.fill(dp, 1);

    int answer = 1;

    for (int i = 0; i < nums.length; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                dp[i] = Math.max(dp[i], 1 + dp[j]);
            }
        }

        answer = Math.max(answer, dp[i]);
    }

    return answer;
}
```

State:

```text
dp[i] = LIS ending at i
```

## 17. Matrix Chain Multiplication

```java
int matrixChainMultiplication(int[] arr) {
    int n = arr.length;
    int[][] dp = new int[n][n];

    for (int length = 2; length < n; length++) {
        for (int i = 1; i + length - 1 < n; i++) {
            int j = i + length - 1;
            dp[i][j] = Integer.MAX_VALUE;

            for (int k = i; k < j; k++) {
                int cost = dp[i][k] + dp[k + 1][j] + arr[i - 1] * arr[k] * arr[j];
                dp[i][j] = Math.min(dp[i][j], cost);
            }
        }
    }

    return dp[1][n - 1];
}
```

State:

```text
dp[i][j] = minimum cost to multiply matrices i through j
```

## 18. Burst Balloons

```java
int maxCoins(int[] nums) {
    int n = nums.length;
    int[] balloons = new int[n + 2];
    balloons[0] = 1;
    balloons[n + 1] = 1;

    for (int i = 0; i < n; i++) {
        balloons[i + 1] = nums[i];
    }

    int[][] dp = new int[n + 2][n + 2];

    for (int length = 2; length < n + 2; length++) {
        for (int left = 0; left + length < n + 2; left++) {
            int right = left + length;

            for (int last = left + 1; last < right; last++) {
                int coins = balloons[left] * balloons[last] * balloons[right]
                        + dp[left][last]
                        + dp[last][right];

                dp[left][right] = Math.max(dp[left][right], coins);
            }
        }
    }

    return dp[0][n + 1];
}
```

Key idea:

```text
Choose the last balloon to burst in an interval.
```

## 19. Egg Dropping

```java
int eggDrop(int eggs, int floors) {
    int[][] dp = new int[eggs + 1][floors + 1];

    for (int e = 1; e <= eggs; e++) {
        dp[e][0] = 0;
        if (floors >= 1) {
            dp[e][1] = 1;
        }
    }

    for (int f = 1; f <= floors; f++) {
        dp[1][f] = f;
    }

    for (int e = 2; e <= eggs; e++) {
        for (int f = 2; f <= floors; f++) {
            dp[e][f] = Integer.MAX_VALUE;

            for (int x = 1; x <= f; x++) {
                int breaks = dp[e - 1][x - 1];
                int doesNotBreak = dp[e][f - x];
                int attempts = 1 + Math.max(breaks, doesNotBreak);
                dp[e][f] = Math.min(dp[e][f], attempts);
            }
        }
    }

    return dp[eggs][floors];
}
```

Time: `O(eggs * floors^2)`

Space: `O(eggs * floors)`

## 20. DP On Trees

State:

```text
For each node, return [take, skip].
take = best answer if this node is robbed.
skip = best answer if this node is not robbed.
```

```java
int[] robTreeState(TreeNode node) {
    if (node == null) {
        return new int[]{0, 0};
    }

    int[] left = robTreeState(node.left);
    int[] right = robTreeState(node.right);

    int take = node.val + left[1] + right[1];
    int skip = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);

    return new int[]{take, skip};
}
```

## 21. Bitmask DP

```java
int minAssignmentCost(int[][] cost) {
    int n = cost.length;
    int totalMasks = 1 << n;
    int[] dp = new int[totalMasks];
    Arrays.fill(dp, Integer.MAX_VALUE / 2);
    dp[0] = 0;

    for (int mask = 0; mask < totalMasks; mask++) {
        int person = Integer.bitCount(mask);

        if (person >= n) {
            continue;
        }

        for (int task = 0; task < n; task++) {
            if ((mask & (1 << task)) == 0) {
                int nextMask = mask | (1 << task);
                dp[nextMask] = Math.min(dp[nextMask], dp[mask] + cost[person][task]);
            }
        }
    }

    return dp[totalMasks - 1];
}
```

Time: `O(n * 2^n)`

Space: `O(2^n)`

