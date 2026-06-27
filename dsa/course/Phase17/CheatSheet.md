# Phase 17 Bit Manipulation Cheat Sheet

## Operators

| Operator | Meaning | Use |
| --- | --- | --- |
| `&` | AND | Check or clear bits |
| `|` | OR | Set bits |
| `^` | XOR | Toggle bits, cancel duplicates |
| `~` | NOT | Flip bits |
| `<<` | Left shift | Create masks |
| `>>` | Signed right shift | Shift right, preserve sign |
| `>>>` | Unsigned right shift | Shift right, fill with zero |

## Core Formulas

| Operation | Formula |
| --- | --- |
| Check bit `i` | `(num & (1 << i)) != 0` |
| Set bit `i` | `num | (1 << i)` |
| Clear bit `i` | `num & ~(1 << i)` |
| Toggle bit `i` | `num ^ (1 << i)` |
| Remove last set bit | `num & (num - 1)` |
| Get last set bit | `num & -num` |
| Power of two | `num > 0 && (num & (num - 1)) == 0` |
| Count selected in mask | `Integer.bitCount(mask)` |
| Total masks for `n` items | `1 << n` |

## XOR Rules

```text
x ^ x = 0
x ^ 0 = x
x ^ y = y ^ x
```

Use XOR when equal values should cancel.

## Common Problems

Single number:

```java
answer ^= num;
```

Missing number:

```java
answer ^= i;
answer ^= nums[i];
```

Hamming distance:

```java
Integer.bitCount(x ^ y)
```

Counting bits:

```java
bits[i] = bits[i >> 1] + (i & 1);
```

## Bitmask Subsets

```java
for (int mask = 0; mask < (1 << n); mask++) {
    for (int bit = 0; bit < n; bit++) {
        if ((mask & (1 << bit)) != 0) {
            // item bit is selected
        }
    }
}
```

Time:

```text
O(n * 2^n)
```

## Bitmask DP Template

```java
int totalMasks = 1 << n;
int[] dp = new int[totalMasks];

for (int mask = 0; mask < totalMasks; mask++) {
    for (int bit = 0; bit < n; bit++) {
        if ((mask & (1 << bit)) == 0) {
            int nextMask = mask | (1 << bit);
            // update dp[nextMask]
        }
    }
}
```

## Java Notes

- `int` has 32 bits.
- Use `1L << bit` for larger masks.
- `>>` preserves sign.
- `>>>` fills with zero.
- `Integer.toBinaryString(num)` prints binary.
- `Integer.bitCount(num)` counts set bits.
- `Integer.numberOfTrailingZeros(num)` finds trailing zero count.

## Mistakes To Avoid

- Forgetting parentheses around shifts: use `(1 << bit)`.
- Forgetting `num > 0` in power-of-two checks.
- Using `int` masks when `n` is too large.
- Confusing bit index with value.
- Using bitmask DP when `n` is too big.
- Forgetting that `1 << n` means `2^n`, not `n^2`.

## Best Interview Sentence

```text
I use a mask where each bit represents whether an item is selected. Checking, adding, or removing an item becomes O(1) with bit operations.
```

