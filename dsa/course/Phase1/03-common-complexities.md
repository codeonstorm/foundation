# 03. Common Time Complexities

This lesson gives you the most common complexities you will see in DSA.

## O(1): Constant Time

The work does not depend on input size.

```java
int getLast(int[] arr) {
    return arr[arr.length - 1];
}
```

Even if the array has one million elements, this reads only one element.

Time complexity: `O(1)`

Common examples:

- Accessing an array index
- Pushing to a stack
- HashMap lookup on average
- Checking if a number is even

## O(log n): Logarithmic Time

The input is repeatedly reduced by a factor, usually half.

```java
int binarySearch(int[] arr, int target) {
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

Every iteration removes half of the remaining search space.

Time complexity: `O(log n)`

Common examples:

- Binary search
- Finding height of balanced binary search tree
- Heap insertion and deletion

## O(n): Linear Time

The work grows directly with input size.

```java
int max(int[] arr) {
    int answer = arr[0];

    for (int num : arr) {
        answer = Math.max(answer, num);
    }

    return answer;
}
```

The loop checks every element once.

Time complexity: `O(n)`

Common examples:

- Sum of array
- Maximum or minimum element
- Linear search
- Counting frequency

## O(n log n): Linearithmic Time

This usually appears in efficient sorting and divide-and-conquer algorithms.

```java
Arrays.sort(arr);
```

For primitive arrays like `int[]`, Java uses a tuned dual-pivot quicksort.
For object arrays like `Integer[]`, Java uses TimSort.

You can generally treat standard comparison sorting as:

```text
O(n log n)
```

Common examples:

- Merge sort
- Heap sort
- Many sorting-based interview solutions

## O(n^2): Quadratic Time

Usually appears when you compare every element with every other element.

```java
boolean hasDuplicateBruteForce(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            if (arr[i] == arr[j]) {
                return true;
            }
        }
    }

    return false;
}
```

The nested loops compare pairs.

Time complexity: `O(n^2)`

Common examples:

- Brute-force pair checking
- Simple matrix-like nested loops
- Bubble sort, selection sort, insertion sort worst case

## O(2^n): Exponential Time

Usually appears when each item has two choices.

```java
void generateSubsets(int[] arr, int index) {
    if (index == arr.length) {
        return;
    }

    generateSubsets(arr, index + 1); // exclude
    generateSubsets(arr, index + 1); // include
}
```

Each index creates two branches.

Time complexity: `O(2^n)`

Common examples:

- Generating all subsets
- Naive recursion with repeated branching
- Some backtracking problems

## O(n!): Factorial Time

Usually appears when generating all permutations.

```java
void permutations(String remaining, String built) {
    if (remaining.length() == 0) {
        System.out.println(built);
        return;
    }

    for (int i = 0; i < remaining.length(); i++) {
        char chosen = remaining.charAt(i);
        String next = remaining.substring(0, i) + remaining.substring(i + 1);
        permutations(next, built + chosen);
    }
}
```

There are `n!` possible orderings of `n` items.

Time complexity: `O(n!)`

Common examples:

- Generate all permutations
- Brute-force traveling salesperson
- Exhaustive arrangement problems

## Growth Order

From best to worst:

```text
O(1)
O(log n)
O(n)
O(n log n)
O(n^2)
O(n^3)
O(2^n)
O(n!)
```

## Important Note

Complexity tells you growth, not exact time.

For small `n`, a simple `O(n^2)` solution may pass.
For large `n`, you usually need `O(n log n)`, `O(n)`, or `O(log n)`.

