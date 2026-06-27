# 03. XOR Patterns

XOR is the star of many bit manipulation problems.

Important rules:

```text
x ^ x = 0
x ^ 0 = x
x ^ y = y ^ x
```

Because equal numbers cancel out, XOR is perfect for duplicate-canceling problems.

## Single Number

Problem:

```text
Every number appears twice except one number.
Find the single number.
```

Example:

```text
[4, 1, 2, 1, 2] -> 4
```

Java:

```java
int singleNumber(int[] nums) {
    int answer = 0;

    for (int num : nums) {
        answer ^= num;
    }

    return answer;
}
```

Why it works:

```text
4 ^ 1 ^ 2 ^ 1 ^ 2
= 4 ^ (1 ^ 1) ^ (2 ^ 2)
= 4 ^ 0 ^ 0
= 4
```

Time: `O(n)`

Space: `O(1)`

## Missing Number

Problem:

```text
Array contains n distinct numbers from 0 to n, with one missing.
Find the missing number.
```

Example:

```text
[3, 0, 1] -> 2
```

Java:

```java
int missingNumber(int[] nums) {
    int n = nums.length;
    int answer = n;

    for (int i = 0; i < n; i++) {
        answer ^= i;
        answer ^= nums[i];
    }

    return answer;
}
```

Why it works:

All matching indices and values cancel. The only value that remains is the missing one.

## Find Two Single Numbers

Problem:

```text
Every number appears twice except two numbers.
Find the two unique numbers.
```

Idea:

1. XOR all numbers. Result is `a ^ b`.
2. Find one bit where `a` and `b` differ.
3. Split numbers into two groups using that bit.
4. XOR each group.

Java:

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

## Single Number When Others Appear Three Times

Problem:

```text
Every number appears three times except one.
Find the single number.
```

Idea:

Count how many times each bit is set.

If a bit count is not divisible by `3`, the single number has that bit.

Java:

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

Space: `O(1)`.

## XOR Swap

You may see this trick:

```java
a = a ^ b;
b = a ^ b;
a = a ^ b;
```

It swaps values without a temporary variable.

For real Java code, prefer the readable version:

```java
int temp = a;
a = b;
b = temp;
```

XOR swap is useful to understand XOR, but not usually recommended for clarity.

## When To Think XOR

Use XOR when:

- Equal values should cancel.
- You need to find an odd occurrence.
- You need to detect bit differences.
- You need to toggle a bit.

