# 01. Number Theory Basics

Number theory is the study of integers and their properties.

In DSA, number theory appears in problems about:

- Divisibility
- Factors
- Prime numbers
- GCD and LCM
- Modular arithmetic
- Counting

## Divisibility

`a` is divisible by `b` if:

```text
a % b == 0
```

Java:

```java
boolean isDivisible(int a, int b) {
    return a % b == 0;
}
```

Example:

```text
12 % 3 == 0
12 is divisible by 3
```

## Factors

A factor of `n` is a number that divides `n`.

Example:

```text
factors of 12: 1, 2, 3, 4, 6, 12
```

Naive factor loop:

```java
List<Integer> factors(int n) {
    List<Integer> answer = new ArrayList<>();

    for (int i = 1; i <= n; i++) {
        if (n % i == 0) {
            answer.add(i);
        }
    }

    return answer;
}
```

Time: `O(n)`

## Factor Loop Up To Square Root

Factors come in pairs.

For `12`:

```text
1 * 12
2 * 6
3 * 4
```

If one factor is greater than `sqrt(n)`, the paired factor is smaller than `sqrt(n)`.

Java:

```java
List<Integer> factorsOptimized(int n) {
    List<Integer> answer = new ArrayList<>();

    for (int i = 1; i * i <= n; i++) {
        if (n % i == 0) {
            answer.add(i);

            if (i != n / i) {
                answer.add(n / i);
            }
        }
    }

    return answer;
}
```

Time: `O(sqrt(n))`

Overflow-safe condition:

```java
for (int i = 1; i <= n / i; i++)
```

This avoids `i * i` overflow.

## Prime Numbers

A prime number has exactly two positive factors:

```text
1 and itself
```

Examples:

```text
2, 3, 5, 7, 11, 13
```

Non-primes:

```text
1 is not prime.
0 is not prime.
Negative numbers are not prime.
```

## Composite Numbers

A composite number has more than two positive factors.

Examples:

```text
4, 6, 8, 9, 10, 12
```

## Even And Odd

```java
boolean isEven(int n) {
    return n % 2 == 0;
}
```

Bit version:

```java
boolean isOdd(int n) {
    return (n & 1) == 1;
}
```

## Overflow Reminder

Java `int` can overflow when numbers become large.

Example:

```java
int x = 1_000_000_000;
int y = x * 3; // overflow
```

Use `long` when multiplying large values:

```java
long y = 1L * x * 3;
```

In math-heavy DSA problems, this small habit saves a lot of bugs.

