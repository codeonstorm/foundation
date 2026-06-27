# 04. Counting Bits And Power Of Two

Counting set bits means counting how many `1`s exist in binary representation.

Example:

```text
13 = 1101
set bits = 3
```

## Simple Count

```java
int countSetBitsSimple(int num) {
    int count = 0;

    while (num > 0) {
        count += num & 1;
        num >>= 1;
    }

    return count;
}
```

Time: `O(number of bits)`

For Java `int`, that is at most 32 iterations.

## Brian Kernighan's Algorithm

Formula:

```java
num = num & (num - 1)
```

This removes the last set bit.

Java:

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

Time:

```text
O(number of set bits)
```

## Java Built-In

Java provides:

```java
Integer.bitCount(num)
```

Example:

```java
int count = Integer.bitCount(13); // 3
```

In interviews, know both the built-in and the manual logic.

## Power Of Two

A positive power of two has exactly one set bit.

Examples:

```text
1  = 0001
2  = 0010
4  = 0100
8  = 1000
16 = 10000
```

Formula:

```java
num > 0 && (num & (num - 1)) == 0
```

Java:

```java
boolean isPowerOfTwo(int num) {
    return num > 0 && (num & (num - 1)) == 0;
}
```

Why it works:

If a number has one set bit, removing its last set bit makes it `0`.

## Counting Bits Problem

Problem:

```text
For every number from 0 to n, return number of set bits.
```

Example:

```text
n = 5
answer = [0, 1, 1, 2, 1, 2]
```

DP relation:

```text
bits[i] = bits[i >> 1] + (i & 1)
```

Meaning:

- `i >> 1` removes the last bit.
- `i & 1` tells whether the last bit was `1`.

Java:

```java
int[] countBits(int n) {
    int[] bits = new int[n + 1];

    for (int i = 1; i <= n; i++) {
        bits[i] = bits[i >> 1] + (i & 1);
    }

    return bits;
}
```

Alternative relation:

```text
bits[i] = bits[i & (i - 1)] + 1
```

because `i & (i - 1)` removes the last set bit.

## Even Or Odd

```java
boolean isOdd(int num) {
    return (num & 1) == 1;
}
```

If the last bit is `1`, the number is odd.

If the last bit is `0`, the number is even.

## Common Counting Bit Uses

- Count selected items in a mask
- Check power of two
- Find Hamming distance
- Bitmask DP
- Generate subsets by number of selected items

