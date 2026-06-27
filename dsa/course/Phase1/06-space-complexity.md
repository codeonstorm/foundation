# 06. Space Complexity

Space complexity measures how extra memory grows with input size.

Important distinction:

- Input space: memory used by the input itself.
- Auxiliary space: extra memory used by your algorithm.

In interviews, when people say space complexity, they usually mean auxiliary space unless they say otherwise.

## O(1) Space

```java
int sum(int[] arr) {
    int total = 0;

    for (int num : arr) {
        total += num;
    }

    return total;
}
```

The input array has `n` elements, but the algorithm only creates one extra variable: `total`.

Space complexity: `O(1)`

## O(n) Space

```java
int[] copyArray(int[] arr) {
    int[] copy = new int[arr.length];

    for (int i = 0; i < arr.length; i++) {
        copy[i] = arr[i];
    }

    return copy;
}
```

The extra array grows with `n`.

Space complexity: `O(n)`

## HashMap Space

```java
Map<Integer, Integer> frequency(int[] arr) {
    Map<Integer, Integer> map = new HashMap<>();

    for (int num : arr) {
        map.put(num, map.getOrDefault(num, 0) + 1);
    }

    return map;
}
```

In the worst case, every number is different.

The map stores `n` keys.

Space complexity: `O(n)`

## Recursion Stack Space

```java
void printDown(int n) {
    if (n == 0) {
        return;
    }

    System.out.println(n);
    printDown(n - 1);
}
```

Before reaching the base case, the JVM call stack stores:

```text
printDown(n)
printDown(n - 1)
printDown(n - 2)
...
printDown(1)
```

Maximum depth: `n`

Space complexity: `O(n)`

## Recursive Binary Search Space

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

Maximum recursion depth: `log n`

Space complexity: `O(log n)`

## Iterative Binary Search Space

```java
int binarySearchIterative(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return mid;
        }

        if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    return -1;
}
```

Only a few integer variables are used.

Space complexity: `O(1)`

## In-Place Algorithm

An algorithm is in-place if it modifies the input directly and uses only constant extra space.

Example:

```java
void reverse(int[] arr) {
    int left = 0;
    int right = arr.length - 1;

    while (left < right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;

        left++;
        right--;
    }
}
```

Time complexity: `O(n)`

Space complexity: `O(1)`

## Space Complexity Checklist

- Did you create an array, list, map, set, stack, or queue?
- Can it grow with input size?
- What is the maximum number of elements stored?
- Does recursion add stack space?
- Are you modifying input in-place?

## Common Space Complexities

| Code pattern | Space |
| --- | --- |
| Few variables | `O(1)` |
| New array of size `n` | `O(n)` |
| HashMap with up to `n` keys | `O(n)` |
| Matrix of `n x m` | `O(n * m)` |
| Recursion depth `n` | `O(n)` |
| Recursion depth `log n` | `O(log n)` |

