# 06. Maximum XOR And Bitmask DP

This lesson covers two important advanced bit patterns:

- Maximum XOR
- DP over subsets using masks

## Maximum XOR Of Two Numbers

Problem:

```text
Given an array, find the maximum value of nums[i] ^ nums[j].
```

Example:

```text
[3, 10, 5, 25, 2, 8] -> 28
```

Because:

```text
5 ^ 25 = 28
```

## Greedy Bit Idea

To maximize XOR, we want higher bits to become `1`.

We try to build the answer from the most significant bit down to the least significant bit.

At each bit:

1. Assume this bit can be `1`.
2. Check whether two prefixes exist that can produce this candidate.
3. If yes, keep the bit.

Java:

```java
int findMaximumXOR(int[] nums) {
    int max = 0;
    int mask = 0;

    for (int bit = 31; bit >= 0; bit--) {
        mask |= (1 << bit);
        Set<Integer> prefixes = new HashSet<>();

        for (int num : nums) {
            prefixes.add(num & mask);
        }

        int candidate = max | (1 << bit);

        for (int prefix : prefixes) {
            if (prefixes.contains(prefix ^ candidate)) {
                max = candidate;
                break;
            }
        }
    }

    return max;
}
```

Time:

```text
O(32 * n) = O(n)
```

Space:

```text
O(n)
```

## Why Prefix Check Works

If:

```text
a ^ b = candidate
```

Then:

```text
a ^ candidate = b
```

So for each prefix `a`, we check whether the matching prefix `b` exists.

## Trie Approach

Maximum XOR can also be solved with a binary trie.

At each bit, try to move opposite to the current bit:

- If current bit is `0`, prefer `1`.
- If current bit is `1`, prefer `0`.

The greedy prefix set method is shorter for learning Phase 17.

## Bitmask DP Basics

Bitmask DP uses a mask as part of the DP state.

Example:

```text
dp[mask] = best answer for the subset represented by mask
```

If there are `n` items:

```text
number of masks = 2^n
```

This is why bitmask DP is used when `n` is small.

## Assignment Problem

Problem:

```text
There are n people and n tasks.
cost[person][task] gives assignment cost.
Assign one task to each person with minimum total cost.
```

State:

```text
dp[mask] = minimum cost after assigning the set of tasks in mask
```

The number of already assigned tasks tells us which person is next:

```java
int person = Integer.bitCount(mask);
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

        if (person == n) {
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

Time:

```text
O(n * 2^n)
```

Space:

```text
O(2^n)
```

## Traveling Salesman Shape

Classic bitmask DP state:

```text
dp[mask][last] = minimum cost to visit cities in mask and end at city last
```

Transition:

```text
try next unvisited city
```

This is more advanced, but the same mask idea applies.

## Bitmask DP Checklist

- What does each bit represent?
- What does `dp[mask]` mean?
- How do I move from one mask to the next?
- Which bit choices are still available?
- What is the starting mask?
- What is the final mask?

## Common Bitmask DP Limits

Rough guide:

| n | masks |
| --- | --- |
| 10 | 1,024 |
| 15 | 32,768 |
| 20 | 1,048,576 |
| 25 | 33,554,432 |

Bitmask DP is usually comfortable up to around `n = 20`, depending on the transition cost.

