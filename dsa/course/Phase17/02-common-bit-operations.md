# 02. Common Bit Operations

Most bit manipulation problems use a small set of operations again and again.

The key mask is:

```java
1 << bit
```

This creates a number where only that bit is `1`.

Example:

```text
1 << 3 = 1000
```

## Check If A Bit Is Set

Question:

```text
Is bit i equal to 1?
```

Formula:

```java
(num & (1 << i)) != 0
```

Java:

```java
boolean isSet(int num, int bit) {
    return (num & (1 << bit)) != 0;
}
```

Why it works:

AND keeps only the target bit. If the result is non-zero, the bit was set.

## Set A Bit

Question:

```text
Make bit i equal to 1.
```

Formula:

```java
num | (1 << i)
```

Java:

```java
int setBit(int num, int bit) {
    return num | (1 << bit);
}
```

OR with `1` sets the target bit.

## Clear A Bit

Question:

```text
Make bit i equal to 0.
```

Formula:

```java
num & ~(1 << i)
```

Java:

```java
int clearBit(int num, int bit) {
    return num & ~(1 << bit);
}
```

`~(1 << i)` creates a mask where every bit is `1` except bit `i`.

AND with that mask clears only the target bit.

## Toggle A Bit

Question:

```text
Flip bit i.
```

Formula:

```java
num ^ (1 << i)
```

Java:

```java
int toggleBit(int num, int bit) {
    return num ^ (1 << bit);
}
```

XOR with `1` flips a bit.

XOR with `0` leaves a bit unchanged.

## Update A Bit

Question:

```text
Set bit i to value 0 or 1.
```

Simple approach:

```java
int updateBit(int num, int bit, int value) {
    num = clearBit(num, bit);
    return num | (value << bit);
}
```

First clear the bit, then place the new value there.

## Remove The Last Set Bit

Formula:

```java
num & (num - 1)
```

Example:

```text
num     = 12 = 1100
num - 1 = 11 = 1011
AND          = 1000
```

Java:

```java
int removeLastSetBit(int num) {
    return num & (num - 1);
}
```

This is useful for counting set bits.

## Get The Last Set Bit

Formula:

```java
num & -num
```

Example:

```text
12 = 1100
last set bit = 0100 = 4
```

Java:

```java
int lastSetBitMask(int num) {
    return num & -num;
}
```

This works because Java integers use two's complement.

## Bit Masks

A bit mask is an integer used to represent a set of boolean choices.

Example:

```text
mask = 10101
```

This can mean:

```text
item 0 is selected
item 2 is selected
item 4 is selected
```

Use masks when:

- There are many yes/no choices
- `n` is small, usually `n <= 20`
- You need to represent subsets compactly
- You need DP over subsets

## Common Mask Operations

| Operation | Code |
| --- | --- |
| Check bit | `(mask & (1 << i)) != 0` |
| Add item | `mask | (1 << i)` |
| Remove item | `mask & ~(1 << i)` |
| Toggle item | `mask ^ (1 << i)` |
| Count selected items | `Integer.bitCount(mask)` |
| Total masks for n items | `1 << n` |

## Java Caution

For `int`, avoid shifting by `31` unless you intentionally want the sign bit.

For larger masks, use `long`:

```java
long mask = 1L << bit;
```

