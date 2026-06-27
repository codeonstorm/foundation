# 04. Analyzing Loops

Loops are the easiest place to start complexity analysis.

Ask:

```text
How many times does this loop run?
```

## Single Loop

```java
void printArray(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        System.out.println(arr[i]);
    }
}
```

The loop runs `n` times.

Time complexity: `O(n)`

## Loop With Step Of 2

```java
void printEverySecond(int[] arr) {
    for (int i = 0; i < arr.length; i += 2) {
        System.out.println(arr[i]);
    }
}
```

The loop runs about `n / 2` times.

Drop constants:

```text
O(n / 2) -> O(n)
```

Time complexity: `O(n)`

## Loop That Doubles

```java
void powersOfTwo(int n) {
    for (int i = 1; i <= n; i *= 2) {
        System.out.println(i);
    }
}
```

Values of `i`:

```text
1, 2, 4, 8, 16, ...
```

The loop runs `log2(n)` times.

Time complexity: `O(log n)`

## Nested Loops Over Same Input

```java
void printPairs(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr.length; j++) {
            System.out.println(arr[i] + ", " + arr[j]);
        }
    }
}
```

Outer loop: `n` times.
Inner loop: `n` times for every outer loop.

Total:

```text
n * n = n^2
```

Time complexity: `O(n^2)`

## Nested Loops Over Different Inputs

```java
void printPairs(int[] a, int[] b) {
    for (int x : a) {
        for (int y : b) {
            System.out.println(x + ", " + y);
        }
    }
}
```

If `a.length = n` and `b.length = m`, then:

```text
O(n * m)
```

## Triangular Loop

```java
void printUniquePairs(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            System.out.println(arr[i] + ", " + arr[j]);
        }
    }
}
```

The inner loop gets shorter each time.

Number of pairs:

```text
(n - 1) + (n - 2) + ... + 1 = n * (n - 1) / 2
```

Big O drops constants and lower terms:

```text
O(n * (n - 1) / 2) -> O(n^2)
```

Time complexity: `O(n^2)`

## Matrix Loop

```java
void printMatrix(int[][] matrix) {
    int rows = matrix.length;
    int cols = matrix[0].length;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            System.out.println(matrix[r][c]);
        }
    }
}
```

If there are `rows` rows and `cols` columns:

```text
O(rows * cols)
```

If the matrix is square with size `n x n`, then:

```text
O(n^2)
```

## Two Pointers

```java
boolean hasPairWithSum(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left < right) {
        int sum = arr[left] + arr[right];

        if (sum == target) {
            return true;
        }

        if (sum < target) {
            left++;
        } else {
            right--;
        }
    }

    return false;
}
```

Even though there are two pointers, each pointer only moves inward.

Total movements are at most `n`.

Time complexity: `O(n)`

## Sliding Window

```java
int maxSumOfSizeK(int[] arr, int k) {
    int windowSum = 0;

    for (int i = 0; i < k; i++) {
        windowSum += arr[i];
    }

    int best = windowSum;

    for (int right = k; right < arr.length; right++) {
        windowSum += arr[right];
        windowSum -= arr[right - k];
        best = Math.max(best, windowSum);
    }

    return best;
}
```

The first loop runs `k` times.
The second loop runs `n - k` times.

Total:

```text
k + (n - k) = n
```

Time complexity: `O(n)`

## Sort Plus Loop

```java
boolean hasDuplicate(int[] arr) {
    Arrays.sort(arr);

    for (int i = 1; i < arr.length; i++) {
        if (arr[i] == arr[i - 1]) {
            return true;
        }
    }

    return false;
}
```

Sorting is `O(n log n)`.
The loop is `O(n)`.

Dominant term:

```text
O(n log n + n) -> O(n log n)
```

Time complexity: `O(n log n)`

## Checklist For Loops

- Count how many times each loop runs.
- Sequential loops add.
- Nested loops multiply.
- Drop constants.
- Keep the fastest-growing term.
- Watch for loops that divide or double.
- Do not call two-pointer code `O(n^2)` just because it has two variables.

