# 05. Factorials, Combinations, And Modular Inverse

## Factorial

Factorial:

```text
n! = n * (n - 1) * ... * 2 * 1
```

Examples:

```text
5! = 120
0! = 1
```

Java:

```java
long factorial(int n) {
    long answer = 1;

    for (int i = 2; i <= n; i++) {
        answer *= i;
    }

    return answer;
}
```

Factorials grow very fast, so use modulo in most DSA problems.

```java
long factorialMod(int n, long mod) {
    long answer = 1;

    for (int i = 2; i <= n; i++) {
        answer = (answer * i) % mod;
    }

    return answer;
}
```

## Permutations

Permutation means arrangement.

Number of ways to arrange `r` items chosen from `n`:

```text
nPr = n! / (n - r)!
```

Java:

```java
long nPr(int n, int r) {
    if (r < 0 || r > n) {
        return 0;
    }

    long answer = 1;

    for (int i = 0; i < r; i++) {
        answer *= (n - i);
    }

    return answer;
}
```

## Combinations

Combination means selection.

Number of ways to choose `r` items from `n`:

```text
nCr = n! / (r! * (n - r)!)
```

Simple Java:

```java
long nCr(int n, int r) {
    if (r < 0 || r > n) {
        return 0;
    }

    r = Math.min(r, n - r);
    long answer = 1;

    for (int i = 1; i <= r; i++) {
        answer = answer * (n - r + i) / i;
    }

    return answer;
}
```

This works for small values, but can overflow for large `n`.

## Modular Inverse Basics

In modulo arithmetic, division is not direct.

Instead of:

```text
a / b mod p
```

we use:

```text
a * inverse(b) mod p
```

If `p` is prime and `b` is not divisible by `p`, Fermat's little theorem says:

```text
inverse(b) = b^(p - 2) mod p
```

Java:

```java
long modInverse(long value, long mod) {
    return modPow(value, mod - 2, mod);
}
```

This requires `mod` to be prime.

## nCr Modulo Prime

Precompute factorials and inverse factorials.

```java
class CombinationMod {
    long mod;
    long[] fact;
    long[] invFact;

    CombinationMod(int n, long mod) {
        this.mod = mod;
        this.fact = new long[n + 1];
        this.invFact = new long[n + 1];

        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = (fact[i - 1] * i) % mod;
        }

        invFact[n] = modPow(fact[n], mod - 2, mod);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = (invFact[i + 1] * (i + 1)) % mod;
        }
    }

    long nCr(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }

        return (((fact[n] * invFact[r]) % mod) * invFact[n - r]) % mod;
    }
}
```

Helper:

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

## When To Use This

Use precomputed factorials when:

- You need many nCr queries.
- `n` can be large.
- Answer must be modulo a prime.

For one small query, simple `nCr` may be enough.

