# Phase 1 Practice

Try these before reading `Solutions.md`.

For each problem, write:

- Time complexity
- Space complexity
- One sentence explaining why

## Part A: Basic Analysis

### 1. First Element

```java
int first(int[] arr) {
    return arr[0];
}
```

### 2. Print All

```java
void printAll(int[] arr) {
    for (int num : arr) {
        System.out.println(num);
    }
}
```

### 3. Print Twice

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

### 4. Nested Same Array

```java
void pairs(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr.length; j++) {
            System.out.println(arr[i] + arr[j]);
        }
    }
}
```

### 5. Nested Different Arrays

```java
void pairs(int[] a, int[] b) {
    for (int x : a) {
        for (int y : b) {
            System.out.println(x + y);
        }
    }
}
```

## Part B: Intermediate Analysis

### 6. Halving Loop

```java
void reduce(int n) {
    while (n > 1) {
        n = n / 2;
    }
}
```

### 7. Doubling Loop

```java
void grow(int n) {
    for (int i = 1; i <= n; i *= 2) {
        System.out.println(i);
    }
}
```

### 8. Triangular Loop

```java
void uniquePairs(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            System.out.println(arr[i] + arr[j]);
        }
    }
}
```

### 9. Sorted Duplicate Check

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

### 10. HashSet Duplicate Check

```java
boolean hasDuplicate(int[] arr) {
    Set<Integer> seen = new HashSet<>();

    for (int num : arr) {
        if (seen.contains(num)) {
            return true;
        }

        seen.add(num);
    }

    return false;
}
```

## Part C: Recursion

### 11. Countdown

```java
void countdown(int n) {
    if (n == 0) {
        return;
    }

    countdown(n - 1);
}
```

### 12. Recursive Binary Search

```java
int search(int[] arr, int left, int right, int target) {
    if (left > right) {
        return -1;
    }

    int mid = left + (right - left) / 2;

    if (arr[mid] == target) {
        return mid;
    }

    if (arr[mid] < target) {
        return search(arr, mid + 1, right, target);
    }

    return search(arr, left, mid - 1, target);
}
```

### 13. Naive Fibonacci

```java
int fib(int n) {
    if (n <= 1) {
        return n;
    }

    return fib(n - 1) + fib(n - 2);
}
```

### 14. Copy Array

```java
int[] copy(int[] arr) {
    int[] result = new int[arr.length];

    for (int i = 0; i < arr.length; i++) {
        result[i] = arr[i];
    }

    return result;
}
```

### 15. Matrix Print

```java
void printMatrix(int[][] matrix) {
    for (int r = 0; r < matrix.length; r++) {
        for (int c = 0; c < matrix[0].length; c++) {
            System.out.println(matrix[r][c]);
        }
    }
}
```

## Challenge Questions

### 16. Two Pointer Pair Sum

The array is sorted.

```java
boolean hasPairSum(int[] arr, int target) {
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

### 17. Build Frequency Map

```java
Map<Integer, Integer> frequency(int[] arr) {
    Map<Integer, Integer> map = new HashMap<>();

    for (int num : arr) {
        map.put(num, map.getOrDefault(num, 0) + 1);
    }

    return map;
}
```

### 18. Triple Nested Loop

```java
void triples(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr.length; j++) {
            for (int k = 0; k < arr.length; k++) {
                System.out.println(arr[i] + arr[j] + arr[k]);
            }
        }
    }
}
```

### 19. Loop Inside Recursive Call

```java
void mystery(int n) {
    if (n <= 1) {
        return;
    }

    for (int i = 0; i < n; i++) {
        System.out.println(i);
    }

    mystery(n / 2);
}
```

### 20. All Subsets Shape

```java
void subsets(int index, int n) {
    if (index == n) {
        return;
    }

    subsets(index + 1, n);
    subsets(index + 1, n);
}
```

### 21. Compare Duplicate Solutions

Compare these two solutions:

```java
boolean duplicateA(int[] arr) {
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

```java
boolean duplicateB(int[] arr) {
    Set<Integer> seen = new HashSet<>();

    for (int num : arr) {
        if (seen.contains(num)) {
            return true;
        }

        seen.add(num);
    }

    return false;
}
```

Write which solution is faster and what tradeoff it makes.

### 22. Compare Pair Sum Solutions

The array is sorted.

Solution A:

```java
boolean pairA(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            if (arr[i] + arr[j] == target) {
                return true;
            }
        }
    }

    return false;
}
```

Solution B:

```java
boolean pairB(int[] arr, int target) {
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

Explain why Solution B is better for a sorted array.
