# Phase 18 Practice Solutions

Use this after attempting `Practice.md`.

## 1. Check Divisibility

```java
boolean isDivisible(int a, int b) {
    return b != 0 && a % b == 0;
}
```

Time: `O(1)`

Space: `O(1)`

## 2. List Factors

```java
List<Integer> factors(int n) {
    List<Integer> answer = new ArrayList<>();

    for (int i = 1; i <= n / i; i++) {
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

Space: `O(number of factors)`

## 3. Check Prime

```java
boolean isPrime(int n) {
    if (n <= 1) {
        return false;
    }

    for (int i = 2; i <= n / i; i++) {
        if (n % i == 0) {
            return false;
        }
    }

    return true;
}
```

Time: `O(sqrt(n))`

## 4. Count Primes

```java
int countPrimes(int n) {
    if (n <= 2) {
        return 0;
    }

    boolean[] isPrime = new boolean[n];
    Arrays.fill(isPrime, true);
    isPrime[0] = false;
    isPrime[1] = false;

    for (int p = 2; p <= (n - 1) / p; p++) {
        if (isPrime[p]) {
            for (int multiple = p * p; multiple < n; multiple += p) {
                isPrime[multiple] = false;
            }
        }
    }

    int count = 0;

    for (boolean prime : isPrime) {
        if (prime) {
            count++;
        }
    }

    return count;
}
```

Time: `O(n log log n)`

Space: `O(n)`

## 5. List Primes

```java
List<Integer> listPrimes(int n) {
    List<Integer> primes = new ArrayList<>();

    if (n <= 2) {
        return primes;
    }

    boolean[] isPrime = new boolean[n];
    Arrays.fill(isPrime, true);
    isPrime[0] = false;
    isPrime[1] = false;

    for (int p = 2; p <= (n - 1) / p; p++) {
        if (isPrime[p]) {
            for (int multiple = p * p; multiple < n; multiple += p) {
                isPrime[multiple] = false;
            }
        }
    }

    for (int i = 2; i < n; i++) {
        if (isPrime[i]) {
            primes.add(i);
        }
    }

    return primes;
}
```

## 6. GCD

```java
int gcd(int a, int b) {
    a = Math.abs(a);
    b = Math.abs(b);

    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }

    return a;
}
```

Time: `O(log min(a, b))`

## 7. LCM

```java
long lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }

    return Math.abs((long) a / gcd(a, b) * b);
}
```

Divide before multiply to reduce overflow risk.

## 8. GCD Of Array

```java
int gcdArray(int[] nums) {
    int answer = 0;

    for (int num : nums) {
        answer = gcd(answer, num);
    }

    return answer;
}
```

## 9. Normalize Mod

```java
int normalizeMod(int value, int mod) {
    return ((value % mod) + mod) % mod;
}
```

This handles negative values.

## 10. Fast Power With Mod

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

## 11. Pow(x, n)

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

Using `long` avoids trouble with `Integer.MIN_VALUE`.

## 12. Factorial Mod

```java
long factorialMod(int n, long mod) {
    long answer = 1;

    for (int i = 2; i <= n; i++) {
        answer = (answer * i) % mod;
    }

    return answer;
}
```

## 13. nPr

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

## 14. nCr Small Values

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

## 15. Modular Inverse

```java
long modInverse(long value, long mod) {
    return modPow(value, mod - 2, mod);
}
```

This requires `mod` to be prime and `value` not divisible by `mod`.

## 16. nCr Modulo Prime

```java
class CombinationMod {
    long mod;
    long[] fact;
    long[] invFact;

    CombinationMod(int n, long mod) {
        this.mod = mod;
        fact = new long[n + 1];
        invFact = new long[n + 1];

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

## 17. Trailing Zeroes

```java
int trailingZeroes(int n) {
    int count = 0;

    while (n > 0) {
        n /= 5;
        count += n;
    }

    return count;
}
```

Count factors of `5`.

## 18. Happy Number

```java
boolean isHappy(int n) {
    Set<Integer> seen = new HashSet<>();

    while (n != 1 && !seen.contains(n)) {
        seen.add(n);
        n = nextHappyValue(n);
    }

    return n == 1;
}

int nextHappyValue(int n) {
    int sum = 0;

    while (n > 0) {
        int digit = n % 10;
        sum += digit * digit;
        n /= 10;
    }

    return sum;
}
```

## 19. Excel Column Number

```java
int titleToNumber(String columnTitle) {
    int answer = 0;

    for (int i = 0; i < columnTitle.length(); i++) {
        int value = columnTitle.charAt(i) - 'A' + 1;
        answer = answer * 26 + value;
    }

    return answer;
}
```

This is base-26 with digits `1..26`.

## 20. Pigeonhole Explanation

There are `n + 1` numbers and only `n` possible values, `1..n`.

If every value appeared at most once, the array could contain at most `n` numbers.

Since it contains `n + 1` numbers, at least one value must appear more than once.

