# 03. Recursion Tree And Complexity

A recursion tree shows how recursive calls branch.

Drawing the tree helps you understand:

- How many calls are made
- How much work happens per call
- Why some recursive solutions are slow

## Linear Recursion Tree

Example:

```java
int factorial(int n) {
    if (n <= 1) {
        return 1;
    }

    return n * factorial(n - 1);
}
```

Tree:

```text
factorial(5)
  factorial(4)
    factorial(3)
      factorial(2)
        factorial(1)
```

There is one branch per call.

Time complexity: `O(n)`

Space complexity: `O(n)`

## Binary Recursion Tree

Naive Fibonacci:

```java
int fib(int n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

Tree for `fib(5)`:

```text
fib(5)
  fib(4)
    fib(3)
      fib(2)
      fib(1)
    fib(2)
  fib(3)
    fib(2)
    fib(1)
```

Many calls repeat.

Time complexity: `O(2^n)`

Space complexity: `O(n)` because the deepest path has length `n`.

## Recursion Tree For Subsets

For every element, we have two choices:

```text
exclude it
include it
```

Code shape:

```java
void subsets(int[] arr, int index) {
    if (index == arr.length) {
        return;
    }

    subsets(arr, index + 1); // exclude
    subsets(arr, index + 1); // include
}
```

Each level doubles the number of calls.

For `n` elements:

```text
Number of subsets = 2^n
```

Time complexity: `O(2^n)` if we only count tree nodes.

If we copy each subset into the answer, time is often `O(n * 2^n)` because each subset can take up to `n` time to copy.

## Recursion Tree For Permutations

For permutations:

- First position has `n` choices.
- Second position has `n - 1` choices.
- Third position has `n - 2` choices.

Total:

```text
n * (n - 1) * (n - 2) * ... * 1 = n!
```

Time complexity is usually `O(n * n!)` if we copy each permutation of length `n`.

## Recurrence Thinking

A recurrence describes recursive work.

### Factorial

```text
T(n) = T(n - 1) + O(1)
```

Answer: `O(n)`

### Binary Search

```text
T(n) = T(n / 2) + O(1)
```

Answer: `O(log n)`

### Fibonacci

```text
T(n) = T(n - 1) + T(n - 2) + O(1)
```

Answer: `O(2^n)`

### Subsets

```text
T(n) = 2T(n - 1) + O(1)
```

Answer: `O(2^n)`

## Complexity Of Backtracking

Backtracking complexity usually depends on:

- Number of choices at each step
- Number of steps
- Cost to validate each choice
- Cost to copy/store each answer

Example:

```text
Subsets: 2 choices for each of n elements -> O(2^n)
Permutations: n choices, then n - 1, then n - 2 -> O(n!)
N-Queens: roughly many board arrangements -> often described as O(n!)
```

## Drawing Before Coding

Before writing recursion, draw:

```text
state
choices
base case
```

Example for subsets of `[1, 2]`:

```text
index 0, []
  exclude 1 -> index 1, []
    exclude 2 -> []
    include 2 -> [2]
  include 1 -> index 1, [1]
    exclude 2 -> [1]
    include 2 -> [1, 2]
```

This tree becomes your code.

