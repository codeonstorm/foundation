# Phase 18 Math For DSA Cheat Sheet

## Prime Check

```java
boolean isPrime(int n) {
    if (n <= 1) return false;

    for (int i = 2; i <= n / i; i++) {
        if (n % i == 0) return false;
    }

    return true;
}
```

Time: `O(sqrt(n))`

## Sieve

```java
for (int p = 2; p <= (n - 1) / p; p++) {
    if (isPrime[p]) {
        for (int multiple = p * p; multiple < n; multiple += p) {
            isPrime[multiple] = false;
        }
    }
}
```

Time: `O(n log log n)`

## GCD

```java
int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return Math.abs(a);
}
```

## LCM

```java
long lcm = Math.abs((long) a / gcd(a, b) * b);
```

Divide before multiply.

## Modulo Rules

```text
(a + b) % m = ((a % m) + (b % m)) % m
(a - b) % m = ((a % m) - (b % m) + m) % m
(a * b) % m = ((a % m) * (b % m)) % m
```

Use `long` for multiplication:

```java
long result = (1L * a * b) % MOD;
```

Normalize negative modulo:

```java
((value % mod) + mod) % mod
```

## Fast Power

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

## Pow(x, n)

Use `long power = n` before negating.

```java
long power = n;
if (power < 0) {
    x = 1 / x;
    power = -power;
}
```

## Factorial Mod

```java
fact[i] = (fact[i - 1] * i) % mod;
```

## Modular Inverse

For prime `mod`:

```java
inverse(x) = x^(mod - 2) mod mod
```

## nCr Mod Prime

```java
nCr = fact[n] * invFact[r] * invFact[n - r] mod MOD
```

## Trailing Zeroes

```java
while (n > 0) {
    n /= 5;
    count += n;
}
```

## Excel Column Number

```java
answer = answer * 26 + (ch - 'A' + 1);
```

## Pigeonhole Principle

```text
More items than boxes means at least one box contains more than one item.
```

## Common Mistakes

- Treating `1` as prime
- Using `i * i <= n` when `i * i` may overflow
- Computing `a * b / gcd` instead of `(a / gcd) * b`
- Forgetting Java negative modulo behavior
- Not using `long` in modular multiplication
- Negating `Integer.MIN_VALUE` as an `int`
- Using modular inverse when modulo is not prime

