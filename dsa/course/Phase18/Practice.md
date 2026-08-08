# Phase 18 Practice

Try these before reading `Solutions.md`.

For every problem, write:

```text
Idea:
Formula or theorem:
Overflow risk:
Time:
Space:
```

## Part A: Number Theory Basics

### 1. Check Divisibility

Write:

```java
boolean isDivisible(int a, int b)
```

### 2. List Factors

Write an optimized function:

```java
List<Integer> factors(int n)
```

Use the square-root idea.

### 3. Check Prime

Write:

```java
boolean isPrime(int n)
```

Use `O(sqrt(n))`.

## Part B: Sieve

### 4. Count Primes

Count primes less than `n`.

Write:

```java
int countPrimes(int n)
```

Use Sieve of Eratosthenes.

### 5. List Primes

Return all primes less than `n`.

Write:

```java
List<Integer> listPrimes(int n)
```

## Part C: GCD And LCM

### 6. GCD

Write:

```java
int gcd(int a, int b)
```

Use the Euclidean algorithm.

### 7. LCM

Write:

```java
long lcm(int a, int b)
```

Handle overflow risk better than `a * b / gcd`.

### 8. GCD Of Array

Write:

```java
int gcdArray(int[] nums)
```

## Part D: Modular Arithmetic And Power

### 9. Normalize Mod

Write:

```java
int normalizeMod(int value, int mod)
```

It should return a non-negative result.

### 10. Fast Power With Mod

Write:

```java
long modPow(long base, long power, long mod)
```

### 11. Pow(x, n)

Write:

```java
double myPow(double x, int n)
```

Handle negative `n`.

## Part E: Factorials And Combinations

### 12. Factorial Mod

Write:

```java
long factorialMod(int n, long mod)
```

### 13. nPr

Write:

```java
long nPr(int n, int r)
```

### 14. nCr Small Values

Write:

```java
long nCr(int n, int r)
```

Use symmetry:

```text
nCr = nC(n-r)
```

### 15. Modular Inverse

For prime `mod`, write:

```java
long modInverse(long value, long mod)
```

### 16. nCr Modulo Prime

Precompute factorials and inverse factorials, then answer:

```java
long nCrModPrime(int n, int r)
```

## Part F: Common Math Problems

### 17. Trailing Zeroes

Write:

```java
int trailingZeroes(int n)
```

### 18. Happy Number

Write:

```java
boolean isHappy(int n)
```

Use cycle detection.

### 19. Excel Column Number

Write:

```java
int titleToNumber(String columnTitle)
```

### 20. Pigeonhole Explanation

Explain why an array of `n + 1` integers where every value is in `1..n` must contain a duplicate.

## Final Reflection

Pick five problems and fill this:

```text
Problem:
Math idea:
Why brute force is slower:
Optimized formula:
Overflow/modulo concern:
```

