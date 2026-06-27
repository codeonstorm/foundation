# 08. Comparing Brute Force And Optimized Solutions

The goal of complexity analysis is not only to label code as `O(n)` or `O(n^2)`.

The real goal is to compare solutions and explain why one approach is better than another.

## What Is A Brute Force Solution?

A brute force solution tries the most direct idea, usually by checking all possibilities.

Brute force is useful because:

- It is often easy to understand.
- It gives you a correct starting point.
- It helps you discover what repeated work is happening.

But brute force can be too slow for large input.

## What Is An Optimized Solution?

An optimized solution removes unnecessary work.

Common optimization tools:

- HashMap or HashSet
- Sorting
- Two pointers
- Sliding window
- Binary search
- Prefix sums
- Memoization

## Example 1: Duplicate Check

Problem:

```text
Given an integer array, return true if any value appears at least twice.
```

### Brute Force

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

Analysis:

- Outer loop runs `n` times.
- Inner loop can run up to `n` times.
- Total time is `O(n^2)`.
- Only a few variables are used, so space is `O(1)`.

Result:

```text
Time: O(n^2)
Space: O(1)
```

### Optimized With HashSet

```java
boolean hasDuplicateOptimized(int[] arr) {
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

Analysis:

- The loop runs `n` times.
- `HashSet.contains` and `HashSet.add` are average `O(1)`.
- The set may store up to `n` values.

Result:

```text
Time: O(n) average
Space: O(n)
```

### Comparison

| Solution | Time | Space | Tradeoff |
| --- | --- | --- | --- |
| Brute force | `O(n^2)` | `O(1)` | Saves memory but slow |
| HashSet | `O(n)` average | `O(n)` | Faster but uses extra memory |

Interview explanation:

```text
The brute force solution compares every pair, so it is O(n^2).
The optimized solution uses a HashSet to remember values we have already seen.
This reduces average time to O(n), but it uses O(n) extra space.
```

## Example 2: Pair Sum

Problem:

```text
Given an array and a target, check whether two numbers add up to the target.
```

### Brute Force

```java
boolean hasPairSumBruteForce(int[] arr, int target) {
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

Result:

```text
Time: O(n^2)
Space: O(1)
```

### Optimized With HashSet

```java
boolean hasPairSumHashSet(int[] arr, int target) {
    Set<Integer> seen = new HashSet<>();

    for (int num : arr) {
        int needed = target - num;

        if (seen.contains(needed)) {
            return true;
        }

        seen.add(num);
    }

    return false;
}
```

Result:

```text
Time: O(n) average
Space: O(n)
```

### Optimized With Two Pointers

If the array is already sorted, use two pointers:

```java
boolean hasPairSumTwoPointers(int[] arr, int target) {
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

Result:

```text
Time: O(n)
Space: O(1)
```

### Comparison

| Situation | Best approach |
| --- | --- |
| Array is unsorted and extra space is allowed | HashSet |
| Array is already sorted | Two pointers |
| Need simplest possible first attempt | Brute force |
| Extra memory is not allowed | Brute force or sort plus two pointers |

If the array is unsorted and you sort it first:

```text
Sort: O(n log n)
Two pointers: O(n)
Total: O(n log n)
```

## Example 3: Fibonacci

Problem:

```text
Find the nth Fibonacci number.
```

### Brute Force Recursion

```java
int fibBruteForce(int n) {
    if (n <= 1) {
        return n;
    }

    return fibBruteForce(n - 1) + fibBruteForce(n - 2);
}
```

Result:

```text
Time: O(2^n)
Space: O(n)
```

Why so slow?

The same values are recomputed again and again.

### Optimized With Memoization

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

Result:

```text
Time: O(n)
Space: O(n)
```

The memo array stores already-solved answers, so each value from `0` to `n` is solved once.

## How To Compare Two Solutions

Use this process:

1. Find the input size.
2. Analyze time complexity.
3. Analyze space complexity.
4. Check whether the faster solution uses more memory.
5. Check whether the optimized solution has extra requirements, such as sorted input.
6. Choose the solution that fits the constraints.

## Constraint-Based Thinking

If `n` is small, brute force may be acceptable.

If `n` is large, brute force usually fails.

Rough guide:

| Input size | Usually acceptable |
| --- | --- |
| `n <= 20` | Exponential may work |
| `n <= 1,000` | `O(n^2)` may work |
| `n <= 100,000` | `O(n log n)` or `O(n)` |
| `n >= 1,000,000` | Usually `O(n)` or `O(log n)` |

This is only a rough guide. Actual limits depend on the platform and operations.

## Good Interview Comparison

```text
The brute force solution checks all pairs, so it takes O(n^2) time and O(1) space.
We can optimize by using a HashSet to store values we have already seen.
That changes the time to O(n) average, but increases space to O(n).
So the optimized solution is better when speed matters and extra memory is allowed.
```

## Milestone

You are ready for the next phase when you can explain:

- Why the brute force solution works
- Why it is slow
- What repeated work exists
- Which data structure or pattern removes that repeated work
- What tradeoff the optimized solution makes

