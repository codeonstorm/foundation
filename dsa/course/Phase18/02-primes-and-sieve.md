# 02. Primes And Sieve Of Eratosthenes

Prime numbers are one of the most common math topics in DSA.

## Check Prime Naively

```java
boolean isPrimeNaive(int n) {
    if (n <= 1) {
        return false;
    }

    for (int i = 2; i < n; i++) {
        if (n % i == 0) {
            return false;
        }
    }

    return true;
}
```

Time: `O(n)`

## Check Prime Up To Square Root

If `n` has a factor greater than `sqrt(n)`, it must also have a factor smaller than `sqrt(n)`.

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

The condition `i <= n / i` avoids `i * i` overflow.

## Count Primes Naively

Problem:

```text
Count prime numbers less than n.
```

Naive:

```java
int countPrimesNaive(int n) {
    int count = 0;

    for (int i = 2; i < n; i++) {
        if (isPrime(i)) {
            count++;
        }
    }

    return count;
}
```

Time: `O(n sqrt(n))`

## Sieve Of Eratosthenes

The sieve efficiently marks non-prime numbers.

Idea:

```text
Start by assuming every number is prime.
For each prime p, mark multiples of p as non-prime.
```

Java:

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

Time:

```text
O(n log log n)
```

Space:

```text
O(n)
```

## Why Start From p * p?

For prime `p`, smaller multiples were already marked by smaller primes.

Example:

```text
For p = 5:
5 * 2, 5 * 3, 5 * 4 were already handled by 2 and 3.
Start from 5 * 5.
```

## List All Primes

```java
List<Integer> listPrimes(int n) {
    boolean[] isPrime = sieve(n);
    List<Integer> primes = new ArrayList<>();

    for (int i = 2; i < n; i++) {
        if (isPrime[i]) {
            primes.add(i);
        }
    }

    return primes;
}
```

Helper:

```java
boolean[] sieve(int n) {
    boolean[] isPrime = new boolean[n];

    if (n <= 2) {
        return isPrime;
    }

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

    return isPrime;
}
```

## When To Use Sieve

Use sieve when:

- You need many prime checks up to a limit.
- You need to count primes below `n`.
- You need all primes up to `n`.

Use square-root primality check when:

- You only need to test one number.

