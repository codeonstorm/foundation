# 04. Modular Arithmetic And Fast Power

Modulo keeps numbers within a fixed range.

In DSA, modulo is usually used because answers can become very large.

Common modulo:

```java
int MOD = 1_000_000_007;
```

This is a large prime.

## Basic Modulo Rules

Addition:

```text
(a + b) % mod = ((a % mod) + (b % mod)) % mod
```

Subtraction:

```text
(a - b) % mod = ((a % mod) - (b % mod) + mod) % mod
```

Multiplication:

```text
(a * b) % mod = ((a % mod) * (b % mod)) % mod
```

Use `long` while multiplying:

```java
long result = (1L * a * b) % MOD;
```

## Negative Modulo In Java

Java can return a negative result:

```java
System.out.println(-3 % 5); // -3
```

Normalize it:

```java
int normalized = ((value % mod) + mod) % mod;
```

## Fast Exponentiation

Problem:

```text
Compute base^power efficiently.
```

Naive:

```text
multiply base power times -> O(power)
```

Fast power uses binary exponentiation.

Idea:

```text
If power is odd, multiply answer by base.
Square base.
Divide power by 2.
```

Java with modulo:

```java
long modPow(long base, long power, long mod) {
    long answer = 1;
    base %= mod;

    while (power > 0) {
        if ((power & 1) == 1) {
            answer = (answer * base) % mod;
        }

        base = (base * base) % mod;
        power >>= 1;
    }

    return answer;
}
```

Time: `O(log power)`

Space: `O(1)`

## Pow(x, n)

Problem:

```text
Implement x^n where n can be negative.
```

Java:

```java
double myPow(double x, int n) {
    long power = n;

    if (power < 0) {
        x = 1 / x;
        power = -power;
    }

    double answer = 1.0;

    while (power > 0) {
        if ((power & 1) == 1) {
            answer *= x;
        }

        x *= x;
        power >>= 1;
    }

    return answer;
}
```

Important:

Use `long power = n`.

Why?

`Integer.MIN_VALUE` cannot be safely negated as an `int`.

## Fast Power Recursion

```java
long modPowRecursive(long base, long power, long mod) {
    if (power == 0) {
        return 1;
    }

    long half = modPowRecursive(base, power / 2, mod);
    long answer = (half * half) % mod;

    if (power % 2 == 1) {
        answer = (answer * base) % mod;
    }

    return answer;
}
```

Time: `O(log power)`

Space: `O(log power)` because of recursion stack.

## When To Use Fast Power

Use fast exponentiation when:

- Exponent is large.
- Modulo is involved.
- You need modular inverse using Fermat's little theorem.
- You need `x^n` in logarithmic time.

