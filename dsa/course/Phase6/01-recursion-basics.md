# 01. Recursion Basics

Recursion means a function calls itself to solve a smaller version of the same problem.

Every recursive solution needs two parts:

- Base case: when to stop.
- Recursive case: how to move toward the base case.

## First Example: Print Numbers

```java
void printIncreasing(int n) {
    if (n == 0) {
        return;
    }

    printIncreasing(n - 1);
    System.out.println(n);
}
```

Call:

```java
printIncreasing(3);
```

Output:

```text
1
2
3
```

Why?

The function first goes down to `0`, then prints while returning.

Call flow:

```text
printIncreasing(3)
  printIncreasing(2)
    printIncreasing(1)
      printIncreasing(0)
    print 1
  print 2
print 3
```

## Base Case

The base case stops recursion.

```java
if (n == 0) {
    return;
}
```

Without a base case, the function keeps calling itself until Java throws `StackOverflowError`.

Bad recursion:

```java
void bad(int n) {
    bad(n - 1);
}
```

This never stops.

## Recursive Case

The recursive case must move closer to the base case.

```java
printIncreasing(n - 1);
```

If `n` becomes smaller every time, it eventually reaches `0`.

## Call Stack

Java uses the call stack to remember active function calls.

For:

```java
printIncreasing(3);
```

The stack grows like this:

```text
printIncreasing(3)
printIncreasing(2)
printIncreasing(1)
printIncreasing(0)
```

Then calls return one by one.

This is why recursion uses extra space.

If recursion depth is `n`, stack space is usually `O(n)`.

## Print Decreasing

```java
void printDecreasing(int n) {
    if (n == 0) {
        return;
    }

    System.out.println(n);
    printDecreasing(n - 1);
}
```

Call:

```java
printDecreasing(3);
```

Output:

```text
3
2
1
```

The print happens before the recursive call.

## Factorial

Factorial:

```text
5! = 5 * 4 * 3 * 2 * 1
```

Recursive idea:

```text
factorial(n) = n * factorial(n - 1)
factorial(1) = 1
```

Java:

```java
int factorial(int n) {
    if (n <= 1) {
        return 1;
    }

    return n * factorial(n - 1);
}
```

For `factorial(5)`:

```text
5 * factorial(4)
5 * 4 * factorial(3)
5 * 4 * 3 * factorial(2)
5 * 4 * 3 * 2 * factorial(1)
5 * 4 * 3 * 2 * 1
```

Time complexity: `O(n)`

Space complexity: `O(n)` because of the call stack.

## Tail Recursion

A recursive function is tail-recursive when the recursive call is the last operation.

Example:

```java
int factorialTail(int n, int answer) {
    if (n <= 1) {
        return answer;
    }

    return factorialTail(n - 1, answer * n);
}
```

The recursive call is the final action.

Important Java note:

```text
Java does not guarantee tail-call optimization.
```

So even tail-recursive Java code still usually uses stack space.

For very deep recursion, prefer an iterative solution if stack overflow is possible.

## How To Think Recursively

Use this checklist:

1. What is the smallest input I can solve directly?
2. What smaller version of the same problem can I call?
3. How do I combine the current answer with the recursive answer?
4. Does every recursive path move toward the base case?

## Common Mistakes

- Missing base case
- Base case is correct but unreachable
- Recursive call does not reduce the problem
- Doing work in the wrong order
- Forgetting recursion stack space

