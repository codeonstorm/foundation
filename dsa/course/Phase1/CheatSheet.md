# Phase 1 Complexity Cheat Sheet

## Big O Rules

- Drop constants: `O(2n)` becomes `O(n)`.
- Drop lower-order terms: `O(n^2 + n)` becomes `O(n^2)`.
- Sequential loops add.
- Nested loops multiply.
- Repeated halving usually means `O(log n)`.
- Sorting usually means `O(n log n)`.
- HashMap and HashSet operations are average `O(1)`.
- Recursion stack counts as space.

## Common Time Complexities

| Complexity | Common source |
| --- | --- |
| `O(1)` | Array access, simple arithmetic |
| `O(log n)` | Binary search, halving loop |
| `O(n)` | Single loop, two pointers, sliding window |
| `O(n log n)` | Sorting, merge sort |
| `O(n^2)` | Nested loops over same input |
| `O(n^3)` | Three nested loops |
| `O(2^n)` | Subsets, naive branching recursion |
| `O(n!)` | Permutations |

## Loop Patterns

```java
for (int i = 0; i < n; i++)
```

Time: `O(n)`

```java
for (int i = 0; i < n; i += 2)
```

Time: `O(n)`

```java
for (int i = 1; i < n; i *= 2)
```

Time: `O(log n)`

```java
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
    }
}
```

Time: `O(n^2)`

## Recursion Patterns

```text
T(n) = T(n - 1) + O(1) -> O(n)
T(n) = T(n / 2) + O(1) -> O(log n)
T(n) = 2T(n / 2) + O(n) -> O(n log n)
T(n) = T(n - 1) + T(n - 2) -> O(2^n)
```

## Space Patterns

| Pattern | Space |
| --- | --- |
| Few variables | `O(1)` |
| New array/list of size `n` | `O(n)` |
| HashMap/HashSet with up to `n` items | `O(n)` |
| Recursion depth `n` | `O(n)` |
| Recursion depth `log n` | `O(log n)` |

## Java Collection Costs

| Collection | Operation | Complexity |
| --- | --- | --- |
| Array | access by index | `O(1)` |
| Array | search unsorted | `O(n)` |
| ArrayList | get/set | `O(1)` |
| ArrayList | add at end | `O(1)` amortized |
| ArrayList | insert/remove middle | `O(n)` |
| HashMap | get/put/containsKey | `O(1)` average |
| HashSet | add/contains/remove | `O(1)` average |
| TreeMap | get/put/remove | `O(log n)` |
| TreeSet | add/contains/remove | `O(log n)` |
| PriorityQueue | add/poll | `O(log n)` |
| PriorityQueue | peek | `O(1)` |
| ArrayDeque | stack/queue operations | `O(1)` |

## Interview Template

```text
Let n be the input size.
The main loop runs n times.
Each iteration does constant work.
So the time complexity is O(n).
The algorithm uses only a few variables, so space complexity is O(1).
```

## Mistakes To Avoid

- Do not say every nested-looking solution is `O(n^2)`.
- Do not forget HashMap and HashSet use extra space.
- Do not ignore recursion stack space.
- Do not call two separate inputs both `n` unless they have the same size.
- Do not include input memory unless asked for total space.
- Do not keep constants in final Big O.

## Comparing Solutions

| Approach | Common time | Common space | Idea |
| --- | --- | --- | --- |
| Brute force pairs | `O(n^2)` | `O(1)` | Check every pair |
| HashSet lookup | `O(n)` average | `O(n)` | Remember seen values |
| Sort plus scan | `O(n log n)` | Depends on sort | Use ordering |
| Two pointers | `O(n)` | `O(1)` | Move inward on sorted data |
| Memoization | Often `O(n)` | Often `O(n)` | Avoid repeated recursion |

Best comparison sentence:

```text
The optimized solution improves time from O(n^2) to O(n), but it uses O(n) extra space.
```
