# Phase 17 Practice Solutions

Use this after attempting `Practice.md`.

## 1. Check Bit

```java
boolean isSet(int num, int bit) {
    return (num & (1 << bit)) != 0;
}
```

Time: `O(1)`

Space: `O(1)`

## 2. Set Bit

```java
int setBit(int num, int bit) {
    return num | (1 << bit);
}
```

OR with `1` sets the target bit.

## 3. Clear Bit

```java
int clearBit(int num, int bit) {
    return num & ~(1 << bit);
}
```

The mask has every bit set except the target bit.

## 4. Toggle Bit

```java
int toggleBit(int num, int bit) {
    return num ^ (1 << bit);
}
```

XOR with `1` flips the target bit.

## 5. Update Bit

```java
int updateBit(int num, int bit, int value) {
    num = num & ~(1 << bit);
    return num | (value << bit);
}
```

First clear, then set to the requested value.

## 6. Count Set Bits

```java
int countSetBits(int num) {
    int count = 0;

    while (num != 0) {
        num = num & (num - 1);
        count++;
    }

    return count;
}
```

Time: `O(number of set bits)`

Space: `O(1)`

## 7. Power Of Two

```java
boolean isPowerOfTwo(int num) {
    return num > 0 && (num & (num - 1)) == 0;
}
```

The `num > 0` check is required because `0 & -1` is `0`, but `0` is not a power of two.

## 8. Counting Bits

```java
int[] countBits(int n) {
    int[] bits = new int[n + 1];

    for (int i = 1; i <= n; i++) {
        bits[i] = bits[i >> 1] + (i & 1);
    }

    return bits;
}
```

Time: `O(n)`

Space: `O(n)`

## 9. Hamming Distance

```java
int hammingDistance(int x, int y) {
    return countSetBits(x ^ y);
}
```

XOR marks bit positions where `x` and `y` differ.

## 10. Single Number

```java
int singleNumber(int[] nums) {
    int answer = 0;

    for (int num : nums) {
        answer ^= num;
    }

    return answer;
}
```

Duplicates cancel because `x ^ x = 0`.

## 11. Missing Number

```java
int missingNumber(int[] nums) {
    int answer = nums.length;

    for (int i = 0; i < nums.length; i++) {
        answer ^= i;
        answer ^= nums[i];
    }

    return answer;
}
```

All present numbers cancel with their matching index.

## 12. Two Single Numbers

```java
int[] twoSingleNumbers(int[] nums) {
    int xor = 0;

    for (int num : nums) {
        xor ^= num;
    }

    int diffBit = xor & -xor;
    int first = 0;
    int second = 0;

    for (int num : nums) {
        if ((num & diffBit) == 0) {
            first ^= num;
        } else {
            second ^= num;
        }
    }

    return new int[]{first, second};
}
```

The differing bit separates the two unique numbers into different groups.

## 13. Single Number Among Triples

```java
int singleNumberAmongTriples(int[] nums) {
    int answer = 0;

    for (int bit = 0; bit < 32; bit++) {
        int count = 0;

        for (int num : nums) {
            if ((num & (1 << bit)) != 0) {
                count++;
            }
        }

        if (count % 3 != 0) {
            answer |= (1 << bit);
        }
    }

    return answer;
}
```

Time: `O(32 * n)`, which is `O(n)`.

Space: `O(1)`

## 14. Generate Subsets

```java
List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> answer = new ArrayList<>();
    int totalMasks = 1 << nums.length;

    for (int mask = 0; mask < totalMasks; mask++) {
        List<Integer> current = new ArrayList<>();

        for (int bit = 0; bit < nums.length; bit++) {
            if ((mask & (1 << bit)) != 0) {
                current.add(nums[bit]);
            }
        }

        answer.add(current);
    }

    return answer;
}
```

Time: `O(n * 2^n)`

## 15. Subset Sums

```java
List<Integer> subsetSums(int[] nums) {
    List<Integer> answer = new ArrayList<>();
    int totalMasks = 1 << nums.length;

    for (int mask = 0; mask < totalMasks; mask++) {
        int sum = 0;

        for (int bit = 0; bit < nums.length; bit++) {
            if ((mask & (1 << bit)) != 0) {
                sum += nums[bit];
            }
        }

        answer.add(sum);
    }

    return answer;
}
```

## 16. Count Selected Items

Manual:

```java
int countSelected(int mask) {
    int count = 0;

    while (mask != 0) {
        mask &= (mask - 1);
        count++;
    }

    return count;
}
```

Built-in:

```java
int countSelectedBuiltIn(int mask) {
    return Integer.bitCount(mask);
}
```

## 17. Maximum XOR

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

Time: `O(n)`

Space: `O(n)`

The constant factor is 32 because Java `int` has 32 bits.

## 18. Minimum Assignment Cost

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

Time: `O(n * 2^n)`

Space: `O(2^n)`

## 19. Explain A Mask

```text
mask = 13
binary = 1101
selected bit positions = 0, 2, 3
```

Because:

```text
13 = 8 + 4 + 1
```

## 20. Debug This Formula

Bug:

```java
return (num & (num - 1)) == 0;
```

This returns true for `0`, but `0` is not a power of two.

Fix:

```java
boolean isPowerOfTwo(int num) {
    return num > 0 && (num & (num - 1)) == 0;
}
```

