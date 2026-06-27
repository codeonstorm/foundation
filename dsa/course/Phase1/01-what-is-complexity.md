# 01. What Is Complexity?

Complexity analysis is the study of how an algorithm behaves as the input size grows.

In DSA, we usually care about two things:

- Time complexity: how the running time grows.
- Space complexity: how the extra memory usage grows.

We do not usually measure exact seconds because seconds depend on the computer, compiler, JVM, operating system, and current load. Instead, we count how the number of operations grows with input size.

## Input Size

The input size is usually written as `n`.

Examples:

| Problem | Input size |
| --- | --- |
| Search in an array | `n = array.length` |
| Reverse a string | `n = string.length()` |
| Traverse a matrix | `n = rows`, `m = columns` |
| Graph problem | `V = vertices`, `E = edges` |
| Sort a list | `n = list.size()` |

## Example 1: Constant Work

```java
int getFirst(int[] arr) {
    return arr[0];
}
```

This function always reads one element.

If the array has 10 elements, it reads one element.
If the array has 10 million elements, it still reads one element.

Time complexity: `O(1)`

## Example 2: Linear Work

```java
int sum(int[] arr) {
    int total = 0;

    for (int num : arr) {
        total += num;
    }

    return total;
}
```

If the array has `n` elements, the loop runs `n` times.

Time complexity: `O(n)`

## Example 3: Quadratic Work

```java
void printAllPairs(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr.length; j++) {
            System.out.println(arr[i] + ", " + arr[j]);
        }
    }
}
```

For every `i`, the inner loop runs `n` times.

Total work:

```text
n * n = n^2
```

Time complexity: `O(n^2)`

## Why Complexity Matters

Suppose one operation takes a tiny amount of time.

| n | O(n) operations | O(n^2) operations |
| --- | --- | --- |
| 10 | 10 | 100 |
| 1,000 | 1,000 | 1,000,000 |
| 100,000 | 100,000 | 10,000,000,000 |

Both `O(n)` and `O(n^2)` may look fine for small input. But when input grows, the difference becomes huge.

## Key Idea

Complexity analysis does not ask:

```text
How fast is this code on my laptop today?
```

It asks:

```text
How does this code scale as input grows?
```

## Quick Check

What is the time complexity?

```java
boolean isEmpty(int[] arr) {
    return arr.length == 0;
}
```

Answer: `O(1)`, because checking the length is constant work.

