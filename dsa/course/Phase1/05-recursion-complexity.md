# 05. Recursion Complexity

Recursive complexity asks:

```text
How many calls are made, and how much work does each call do?
```

## Basic Recursion

```java
int factorial(int n) {
    if (n <= 1) {
        return 1;
    }

    return n * factorial(n - 1);
}
```

Calls:

```text
factorial(n)
factorial(n - 1)
factorial(n - 2)
...
factorial(1)
```

There are `n` calls.
Each call does constant work.

Time complexity: `O(n)`

Space complexity: `O(n)` because the call stack stores `n` active calls.

## Recursive Binary Search

```java
int binarySearch(int[] arr, int left, int right, int target) {
    if (left > right) {
        return -1;
    }

    int mid = left + (right - left) / 2;

    if (arr[mid] == target) {
        return mid;
    }

    if (arr[mid] < target) {
        return binarySearch(arr, mid + 1, right, target);
    }

    return binarySearch(arr, left, mid - 1, target);
}
```

Each call cuts the input in half.

Number of calls:

```text
O(log n)
```

Time complexity: `O(log n)`

Space complexity: `O(log n)` because of recursion stack.

The iterative version of binary search has `O(1)` extra space.

## Naive Fibonacci

```java
int fib(int n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

This looks small, but it repeats a lot of work.

Example:

```text
fib(5)
fib(4) + fib(3)
fib(3) + fib(2) + fib(2) + fib(1)
...
```

Many values are calculated again and again.

Time complexity: `O(2^n)`

Space complexity: `O(n)` for the deepest call stack.

## Fibonacci With Memoization

```java
int fibMemo(int n, int[] memo) {
    if (n <= 1) {
        return n;
    }

    if (memo[n] != -1) {
        return memo[n];
    }

    memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    return memo[n];
}
```

Each Fibonacci value from `0` to `n` is solved once.

Time complexity: `O(n)`

Space complexity:

- `O(n)` for the memo array
- `O(n)` for recursion stack
- Overall: `O(n)`

## Merge Sort

```java
void mergeSort(int[] arr, int left, int right) {
    if (left >= right) {
        return;
    }

    int mid = left + (right - left) / 2;

    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);
    merge(arr, left, mid, right);
}
```

Merge sort splits the array into halves.

There are `log n` levels of splitting.
At each level, merging touches all `n` elements.

Time complexity:

```text
O(n log n)
```

Space complexity:

```text
O(n)
```

because merge sort usually needs temporary arrays for merging.

## Simple Recurrence Thinking

A recurrence is a formula for recursive work.

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

### Merge Sort

```text
T(n) = 2T(n / 2) + O(n)
```

Answer: `O(n log n)`

### Naive Fibonacci

```text
T(n) = T(n - 1) + T(n - 2) + O(1)
```

Answer: `O(2^n)`

## Recursion Analysis Checklist

- What is the base case?
- How many recursive calls does each call make?
- How much smaller is the input in each call?
- How much non-recursive work happens in each call?
- What is the maximum recursion depth?
- Is the same subproblem repeated?

## Key Interview Sentence

```text
The recursion depth is n, and each call does constant work, so the time is O(n) and the recursion stack space is O(n).
```

