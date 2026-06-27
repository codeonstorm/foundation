# 08. Advanced DP Patterns

This lesson gives you a practical first pass through advanced DP patterns. You do not need to master all of them in one day, but you should know how to recognize their states.

## Longest Increasing Subsequence

Problem:

```text
Given an array, find the length of the longest strictly increasing subsequence.
```

State:

```text
dp[i] = length of LIS ending at index i
```

Transition:

```text
if nums[j] < nums[i]:
    dp[i] = max(dp[i], 1 + dp[j])
```

Java:

```java
int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n];
    Arrays.fill(dp, 1);

    int answer = 1;

    for (int i = 0; i < n; i++) {
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

Time: `O(n^2)`

Space: `O(n)`

There is also an `O(n log n)` binary-search method, but the DP version is the foundation.

## Matrix Chain Multiplication

Problem:

```text
Given matrices with dimensions from an array arr.
Matrix i has size arr[i - 1] x arr[i].
Find minimum multiplication cost.
```

State:

```text
dp[i][j] = minimum cost to multiply matrices from i to j
```

Transition:

```text
try every split k from i to j - 1
cost = dp[i][k] + dp[k + 1][j] + arr[i - 1] * arr[k] * arr[j]
```

Java:

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

Time: `O(n^3)`

Space: `O(n^2)`

## Burst Balloons

Problem:

```text
Burst balloons to maximize coins.
When balloon i bursts, coins gained are nums[left] * nums[i] * nums[right].
```

Key DP idea:

Instead of thinking which balloon bursts first, think which balloon bursts last in an interval.

State:

```text
dp[left][right] = max coins from bursting balloons strictly between left and right
```

Java:

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

Time: `O(n^3)`

Space: `O(n^2)`

## Egg Dropping

Problem:

```text
Given eggs and floors, find the minimum attempts needed in the worst case.
```

State:

```text
dp[e][f] = minimum attempts needed with e eggs and f floors
```

Transition:

If you drop from floor `x`:

- Egg breaks: solve below with `e - 1` eggs and `x - 1` floors
- Egg does not break: solve above with `e` eggs and `f - x` floors

Worst case:

```text
1 + max(breaks, doesNotBreak)
```

Java:

```java
int eggDrop(int eggs, int floors) {
    int[][] dp = new int[eggs + 1][floors + 1];

    for (int e = 1; e <= eggs; e++) {
        dp[e][0] = 0;
        dp[e][1] = 1;
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

## DP On Trees

Tree DP means each node returns information to its parent.

Example problem:

```text
Find maximum path sum in a binary tree.
```

At each node:

- Ask left subtree for best gain.
- Ask right subtree for best gain.
- Update global answer with path through current node.
- Return best single-side gain to parent.

Simpler example: height of tree.

```java
int height(TreeNode root) {
    if (root == null) {
        return 0;
    }

    int left = height(root.left);
    int right = height(root.right);

    return 1 + Math.max(left, right);
}
```

State in words:

```text
The answer returned by a node describes the best result for the subtree rooted at that node.
```

## House Robber On Tree

For each node, return two values:

```text
take = best answer if we rob this node
skip = best answer if we skip this node
```

```java
int robTree(TreeNode root) {
    int[] result = robTreeState(root);
    return Math.max(result[0], result[1]);
}

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

## DP With Bitmasking

Bitmask DP is used when a subset of items is part of the state.

Example state:

```text
dp[mask] = best answer after using the set of items represented by mask
```

If there are `n` items, there are:

```text
2^n masks
```

## Assignment Problem Shape

Problem shape:

```text
Assign one task to each person with minimum cost.
```

State:

```text
mask = which tasks have already been assigned
person = number of assigned tasks = bitCount(mask)
```

Java:

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

## Advanced DP Recognition

| Pattern | State |
| --- | --- |
| LIS | `index` |
| Matrix chain | `left, right` |
| Burst balloons | `left, right` interval |
| Egg dropping | `eggs, floors` |
| Tree DP | subtree rooted at node |
| Bitmask DP | subset mask |

