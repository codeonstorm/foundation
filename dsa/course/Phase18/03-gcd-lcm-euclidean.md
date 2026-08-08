# 03. GCD, LCM, And Euclidean Algorithm

## GCD

GCD means greatest common divisor.

Example:

```text
gcd(12, 18) = 6
```

because `6` is the largest number that divides both.

## Naive GCD

```java
int gcdNaive(int a, int b) {
    int answer = 1;

    for (int i = 1; i <= Math.min(a, b); i++) {
        if (a % i == 0 && b % i == 0) {
            answer = i;
        }
    }

    return answer;
}
```

Time: `O(min(a, b))`

## Euclidean Algorithm

Key identity:

```text
gcd(a, b) = gcd(b, a % b)
```

Base case:

```text
gcd(a, 0) = a
```

Java:

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

Recursive:

```java
int gcdRecursive(int a, int b) {
    if (b == 0) {
        return Math.abs(a);
    }

    return gcdRecursive(b, a % b);
}
```

Time:

```text
O(log min(a, b))
```

## LCM

LCM means least common multiple.

Example:

```text
lcm(12, 18) = 36
```

Formula:

```text
lcm(a, b) = abs(a * b) / gcd(a, b)
```

Overflow-safe Java:

```java
long lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }

    return Math.abs((long) a / gcd(a, b) * b);
}
```

Why divide before multiply?

```text
(a / gcd) * b
```

reduces the chance of overflow compared to:

```text
a * b / gcd
```

## GCD Of Array

```java
int gcdArray(int[] nums) {
    int answer = 0;

    for (int num : nums) {
        answer = gcd(answer, num);
    }

    return answer;
}
```

Why start with `0`?

```text
gcd(0, x) = x
```

## Common Uses

Use GCD and LCM for:

- Simplifying fractions
- Divisibility problems
- Repeating cycles
- Grid movement ratios
- Counting common multiples
- Number theory constraints

## Common Mistakes

- Forgetting `a` or `b` can be `0`
- Multiplying before dividing in LCM
- Not using `long` for large products
- Confusing GCD with LCM

