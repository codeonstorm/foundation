# 01. Binary And Bitwise Operators

Computers store integers in binary.

Decimal:

```text
13
```

Binary:

```text
1101
```

Because:

```text
13 = 8 + 4 + 1
13 = 2^3 + 2^2 + 2^0
```

## Bits And Positions

Bits are usually counted from right to left, starting at `0`.

```text
binary:  1 1 0 1
index:   3 2 1 0
```

The bit at index `i` represents:

```text
2^i
```

## Java Integer Note

Java `int` uses 32 bits.

Example:

```java
int x = 13;
System.out.println(Integer.toBinaryString(x)); // 1101
```

Negative numbers use two's complement representation. For most DSA bit problems, inputs are non-negative unless stated otherwise.

## AND Operator: `&`

AND returns `1` only when both bits are `1`.

```text
1 & 1 = 1
1 & 0 = 0
0 & 1 = 0
0 & 0 = 0
```

Example:

```text
12 = 1100
10 = 1010
----------
&  = 1000 = 8
```

Java:

```java
int result = 12 & 10; // 8
```

Use AND for:

- Checking whether a bit is set
- Clearing bits
- Applying masks

## OR Operator: `|`

OR returns `1` if at least one bit is `1`.

```text
1 | 1 = 1
1 | 0 = 1
0 | 1 = 1
0 | 0 = 0
```

Example:

```text
12 = 1100
10 = 1010
----------
|  = 1110 = 14
```

Java:

```java
int result = 12 | 10; // 14
```

Use OR for:

- Setting bits
- Combining flags

## XOR Operator: `^`

XOR returns `1` when bits are different.

```text
1 ^ 1 = 0
1 ^ 0 = 1
0 ^ 1 = 1
0 ^ 0 = 0
```

Example:

```text
12 = 1100
10 = 1010
----------
^  = 0110 = 6
```

Java:

```java
int result = 12 ^ 10; // 6
```

Important XOR properties:

```text
x ^ x = 0
x ^ 0 = x
x ^ y = y ^ x
```

Use XOR for:

- Single number problems
- Missing number
- Toggling bits
- Comparing bit differences

## NOT Operator: `~`

NOT flips all bits.

```text
~0 = 1
~1 = 0
```

Java:

```java
int x = 5;
int y = ~x;
```

Because Java `int` has 32 bits, `~5` is not simply a small positive number. It becomes negative due to two's complement.

In DSA, `~` is often used with a mask:

```java
num & ~(1 << bit)
```

This clears a bit.

## Left Shift: `<<`

Left shift moves bits left and fills with zeroes on the right.

```text
1 << 0 = 1
1 << 1 = 2
1 << 2 = 4
1 << 3 = 8
```

In general:

```text
1 << i = 2^i
```

Java:

```java
int mask = 1 << 3; // 8
```

Use left shift to create masks.

## Right Shift: `>>`

Right shift moves bits right.

For positive numbers:

```text
8 >> 1 = 4
8 >> 2 = 2
```

This is similar to integer division by powers of two.

Java:

```java
int x = 8 >> 1; // 4
```

## Unsigned Right Shift: `>>>`

Java has two right shifts:

| Operator | Meaning |
| --- | --- |
| `>>` | Signed right shift, preserves sign bit |
| `>>>` | Unsigned right shift, fills left side with `0` |

For non-negative numbers, `>>` and `>>>` behave the same.

For negative numbers, they differ.

Most beginner DSA bit problems use non-negative numbers, so `>>` is usually enough. Use `>>>` when you specifically need unsigned bit movement.

## Operator Summary

| Operator | Name | Common use |
| --- | --- | --- |
| `&` | AND | Check or clear bits |
| `|` | OR | Set bits |
| `^` | XOR | Toggle bits, cancel duplicates |
| `~` | NOT | Flip bits |
| `<<` | Left shift | Build masks, multiply by powers of two |
| `>>` | Right shift | Divide by powers of two, scan bits |
| `>>>` | Unsigned right shift | Shift without preserving sign |

