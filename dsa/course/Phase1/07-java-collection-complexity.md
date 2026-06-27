# 07. Java Collection Complexity

Java collections are useful, but each operation has a cost. Knowing these costs helps you analyze real DSA solutions.

## Array

```java
int[] arr = new int[10];
```

| Operation | Complexity |
| --- | --- |
| Access by index | `O(1)` |
| Update by index | `O(1)` |
| Search unsorted array | `O(n)` |
| Insert in middle manually | `O(n)` |
| Delete from middle manually | `O(n)` |

Why middle insert/delete is `O(n)`:

Elements must be shifted.

## ArrayList

```java
List<Integer> list = new ArrayList<>();
```

| Operation | Average Complexity |
| --- | --- |
| `get(index)` | `O(1)` |
| `set(index, value)` | `O(1)` |
| `add(value)` at end | `O(1)` amortized |
| `add(index, value)` | `O(n)` |
| `remove(index)` | `O(n)` |
| `contains(value)` | `O(n)` |

`add(value)` is amortized `O(1)` because sometimes the internal array must resize, but over many insertions the average cost is constant.

## LinkedList

```java
List<Integer> list = new LinkedList<>();
```

| Operation | Complexity |
| --- | --- |
| Add first or last | `O(1)` |
| Remove first or last | `O(1)` |
| Get by index | `O(n)` |
| Search by value | `O(n)` |

Important:

`LinkedList` is not automatically faster than `ArrayList`. For most DSA problems, `ArrayList` is preferred unless you specifically need queue/deque behavior.

## HashMap

```java
Map<Integer, Integer> map = new HashMap<>();
```

| Operation | Average Complexity | Worst Case |
| --- | --- | --- |
| `put(key, value)` | `O(1)` | `O(n)` |
| `get(key)` | `O(1)` | `O(n)` |
| `containsKey(key)` | `O(1)` | `O(n)` |
| `remove(key)` | `O(1)` | `O(n)` |

In normal interview analysis, we usually say HashMap operations are `O(1)` average.

Space complexity: `O(n)` if it stores up to `n` keys.

## HashSet

```java
Set<Integer> set = new HashSet<>();
```

| Operation | Average Complexity |
| --- | --- |
| `add(value)` | `O(1)` |
| `contains(value)` | `O(1)` |
| `remove(value)` | `O(1)` |

Useful for duplicate detection and fast membership checks.

## TreeMap And TreeSet

```java
Map<Integer, String> map = new TreeMap<>();
Set<Integer> set = new TreeSet<>();
```

| Operation | Complexity |
| --- | --- |
| Insert | `O(log n)` |
| Search | `O(log n)` |
| Delete | `O(log n)` |

These keep keys sorted.

Use them when you need sorted order, floor, ceiling, lower, or higher operations.

## PriorityQueue

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
```

| Operation | Complexity |
| --- | --- |
| `peek()` | `O(1)` |
| `add(value)` | `O(log n)` |
| `poll()` | `O(log n)` |

Useful for top K, scheduling, shortest path, and heap problems.

## Stack And Queue Choices

Prefer `ArrayDeque` for stack and queue behavior.

```java
Deque<Integer> stack = new ArrayDeque<>();
Deque<Integer> queue = new ArrayDeque<>();
```

| Operation | Complexity |
| --- | --- |
| `push`, `pop`, `peek` | `O(1)` |
| `addLast`, `removeFirst`, `peekFirst` | `O(1)` |

Avoid the old `Stack` class in modern Java unless a platform specifically requires it.

## Sorting

```java
Arrays.sort(arr);
Collections.sort(list);
```

| Operation | Complexity |
| --- | --- |
| Sort primitive array | Usually `O(n log n)` |
| Sort object array | `O(n log n)` |
| Sort list | `O(n log n)` |

In DSA answers, standard sorting is usually considered `O(n log n)`.

## Common Interview Examples

### Duplicate Check With HashSet

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

Time complexity: `O(n)` average

Space complexity: `O(n)`

### Top K With PriorityQueue

```java
int kthLargest(int[] arr, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int num : arr) {
        minHeap.add(num);

        if (minHeap.size() > k) {
            minHeap.poll();
        }
    }

    return minHeap.peek();
}
```

Each heap operation costs `O(log k)` because the heap size is kept at `k`.

Time complexity: `O(n log k)`

Space complexity: `O(k)`

