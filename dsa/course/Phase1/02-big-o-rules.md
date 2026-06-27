# 02. Big O Rules

Big O notation describes the upper bound of an algorithm's growth.

In simple words:

```text
Big O tells us how bad the algorithm can get as input grows.
```

## Common Notations

| Notation | Meaning |
| --- | --- |
| `O(...)` | Upper bound, commonly used for worst-case complexity |
| `Omega(...)` | Lower bound, best-case style reasoning |
| `Theta(...)` | Tight bound, both upper and lower bound |

In most DSA interviews, when someone asks for complexity, they usually expect Big O.

## Rule 1: Drop Constants

```java
void printTwice(int[] arr) {
    for (int num : arr) {
        System.out.println(num);
    }

    for (int num : arr) {
        System.out.println(num);
    }
}
```

The first loop runs `n` times.
The second loop runs `n` times.

Total:

```text
n + n = 2n
```

Big O drops constants:

```text
O(2n) -> O(n)
```

Time complexity: `O(n)`

## Rule 2: Keep The Dominant Term

```java
void example(int[] arr) {
    for (int num : arr) {
        System.out.println(num);
    }

    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr.length; j++) {
            System.out.println(arr[i] + arr[j]);
        }
    }
}
```

The first loop is `O(n)`.
The nested loop is `O(n^2)`.

Total:

```text
O(n + n^2)
```

For large `n`, `n^2` dominates `n`.

Time complexity: `O(n^2)`

## Rule 3: Sequential Blocks Add

```java
void process(int[] a, int[] b) {
    for (int x : a) {
        System.out.println(x);
    }

    for (int y : b) {
        System.out.println(y);
    }
}
```

If `a.length = n` and `b.length = m`, then:

```text
O(n + m)
```

Do not simplify this to `O(n)` unless both arrays are known to have the same size.

## Rule 4: Nested Blocks Multiply

```java
void pairs(int[] a, int[] b) {
    for (int x : a) {
        for (int y : b) {
            System.out.println(x + y);
        }
    }
}
```

Outer loop runs `n` times.
Inner loop runs `m` times for each outer iteration.

Total:

```text
O(n * m)
```

If both arrays have the same size, this becomes `O(n^2)`.

## Rule 5: Half Still Counts As Linear

```java
void printHalf(int[] arr) {
    for (int i = 0; i < arr.length / 2; i++) {
        System.out.println(arr[i]);
    }
}
```

The loop runs `n / 2` times.

Big O drops constants:

```text
O(n / 2) -> O(n)
```

Time complexity: `O(n)`

## Rule 6: Logarithmic Means Repeated Division

```java
void divideByTwo(int n) {
    while (n > 1) {
        n = n / 2;
    }
}
```

Each step cuts `n` in half.

Example:

```text
16 -> 8 -> 4 -> 2 -> 1
```

Number of steps is `log2(n)`.

Time complexity: `O(log n)`

## Rule 7: Different Inputs Need Different Variables

```java
boolean containsCommonValue(int[] a, int[] b) {
    for (int x : a) {
        for (int y : b) {
            if (x == y) {
                return true;
            }
        }
    }

    return false;
}
```

If `a.length = n` and `b.length = m`, worst-case time is:

```text
O(n * m)
```

Calling it `O(n^2)` is only correct if both arrays are the same size.

## Best, Average, And Worst Case

Example:

```java
boolean contains(int[] arr, int target) {
    for (int num : arr) {
        if (num == target) {
            return true;
        }
    }

    return false;
}
```

| Case | Explanation | Complexity |
| --- | --- | --- |
| Best | Target is first element | `O(1)` |
| Worst | Target is last or missing | `O(n)` |
| Average | Target may be anywhere | `O(n)` |

Interviews usually ask for worst-case unless stated otherwise.

## Key Interview Sentence

```text
We ignore constants and lower-order terms because Big O describes growth rate, not exact operation count.
```

