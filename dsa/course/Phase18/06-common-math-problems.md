# 06. Common Math Problems

This lesson covers common math patterns from interviews.

## Trailing Zeroes In Factorial

Problem:

```text
Count trailing zeroes in n!
```

Trailing zeroes come from factors of `10`.

```text
10 = 2 * 5
```

There are usually more factors of `2` than `5`, so count factors of `5`.

Java:

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

Example:

```text
25! has floor(25/5) + floor(25/25) = 5 + 1 = 6 trailing zeroes
```

Time: `O(log n)`

## Happy Number

Problem:

```text
Repeatedly replace a number by the sum of squares of its digits.
If it becomes 1, it is happy.
If it loops forever, it is not happy.
```

Use cycle detection with a set:

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

Floyd cycle detection version:

```java
boolean isHappyFloyd(int n) {
    int slow = n;
    int fast = nextHappyValue(n);

    while (fast != 1 && slow != fast) {
        slow = nextHappyValue(slow);
        fast = nextHappyValue(nextHappyValue(fast));
    }

    return fast == 1;
}
```

## Excel Column Number

Problem:

```text
"A" -> 1
"Z" -> 26
"AA" -> 27
"AB" -> 28
```

This is base-26, but digits are `1` to `26` instead of `0` to `25`.

Java:

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

## Pow(x, n)

Covered in the fast power lesson, but it is a common interview problem.

Key issue:

```text
n may be negative and may equal Integer.MIN_VALUE.
```

Use `long` for exponent:

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

## Pigeonhole Principle

Basic idea:

```text
If you put more than n items into n boxes, at least one box has more than one item.
```

Example:

```text
In a group of 13 people, at least two people were born in the same month.
```

Why?

```text
13 people, 12 months.
```

DSA uses:

- Duplicate detection
- Remainder problems
- Proof that a collision must exist
- Cycle detection reasoning

Example:

```text
If an array has n + 1 numbers and every number is in the range 1..n, at least one duplicate must exist.
```

There are `n + 1` numbers and only `n` possible values.

## Common Math Problem Checklist

- Is the problem about divisibility?
- Is there a prime or GCD hidden in the statement?
- Can I avoid overflow with `long`?
- Is modulo required after every multiplication?
- Is the problem secretly base conversion?
- Is cycle detection needed?
- Does pigeonhole prove a duplicate or collision exists?

