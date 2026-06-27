# Phase 1 Practice Solutions

Use these explanations after trying `Practice.md`.

## 1. First Element

Time: `O(1)`

Space: `O(1)`

Reason: It reads one array position and uses no growing extra memory.

## 2. Print All

Time: `O(n)`

Space: `O(1)`

Reason: The loop visits every element once.

## 3. Print Twice

Time: `O(n)`

Space: `O(1)`

Reason: Two separate loops do `2n` work, and constants are dropped.

## 4. Nested Same Array

Time: `O(n^2)`

Space: `O(1)`

Reason: For every element, the inner loop scans all `n` elements.

## 5. Nested Different Arrays

Time: `O(n * m)`

Space: `O(1)`

Reason: If `a` has `n` elements and `b` has `m`, every pair is printed.

## 6. Halving Loop

Time: `O(log n)`

Space: `O(1)`

Reason: `n` is divided by 2 each iteration.

## 7. Doubling Loop

Time: `O(log n)`

Space: `O(1)`

Reason: `i` doubles each iteration until it passes `n`.

## 8. Triangular Loop

Time: `O(n^2)`

Space: `O(1)`

Reason: The number of pairs is `n * (n - 1) / 2`, which simplifies to `O(n^2)`.

## 9. Sorted Duplicate Check

Time: `O(n log n)`

Space: usually `O(1)` to `O(n)` depending on the sort implementation and data type

Reason: Sorting dominates the linear scan.

For interview simplicity, say:

```text
Time is O(n log n). Extra space depends on the sorting implementation.
```

## 10. HashSet Duplicate Check

Time: `O(n)` average

Space: `O(n)`

Reason: Each HashSet lookup and insert is average `O(1)`, and the set may store all elements.

## 11. Countdown

Time: `O(n)`

Space: `O(n)`

Reason: There are `n` recursive calls, and the call stack grows to depth `n`.

## 12. Recursive Binary Search

Time: `O(log n)`

Space: `O(log n)`

Reason: Each call halves the search range, and the recursion stack depth is `log n`.

## 13. Naive Fibonacci

Time: `O(2^n)`

Space: `O(n)`

Reason: Each call branches into two more calls, but the deepest path has length `n`.

## 14. Copy Array

Time: `O(n)`

Space: `O(n)`

Reason: It copies all elements into a new array of the same size.

## 15. Matrix Print

Time: `O(rows * cols)`

Space: `O(1)`

Reason: Every matrix cell is printed once.

For a square `n x n` matrix, time is `O(n^2)`.

## 16. Two Pointer Pair Sum

Time: `O(n)`

Space: `O(1)`

Reason: Each pointer only moves inward. The total number of pointer moves is at most `n`.

## 17. Build Frequency Map

Time: `O(n)` average

Space: `O(n)`

Reason: The loop visits every element once, and the map may store `n` unique keys.

## 18. Triple Nested Loop

Time: `O(n^3)`

Space: `O(1)`

Reason: Three nested loops each run `n` times.

## 19. Loop Inside Recursive Call

Time: `O(n)`

Space: `O(log n)`

Reason:

```text
T(n) = n + n/2 + n/4 + n/8 + ...
```

This geometric series sums to less than `2n`, so time is `O(n)`.

The recursion depth is `O(log n)` because `n` is halved each time.

## 20. All Subsets Shape

Time: `O(2^n)`

Space: `O(n)`

Reason: Each level branches into two recursive calls, and the maximum recursion depth is `n`.

## 21. Compare Duplicate Solutions

Solution A:

```text
Time: O(n^2)
Space: O(1)
```

Solution B:

```text
Time: O(n) average
Space: O(n)
```

Solution B is faster because it uses a HashSet to check whether a value was already seen in average `O(1)` time.

Tradeoff: Solution B uses extra memory.

Good explanation:

```text
The brute force solution checks every pair, so it is O(n^2).
The HashSet solution checks each value once and uses fast lookup, so it is O(n) average.
It improves time by using O(n) extra space.
```

## 22. Compare Pair Sum Solutions

Solution A:

```text
Time: O(n^2)
Space: O(1)
```

Solution B:

```text
Time: O(n)
Space: O(1)
```

Solution B is better because the array is sorted. If the current sum is too small, moving `left` right increases the sum. If the current sum is too large, moving `right` left decreases the sum.

Each pointer moves at most `n` steps total, so the algorithm is linear.
