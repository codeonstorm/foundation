# Phase 6 Recursion And Backtracking Cheat Sheet

## Recursion Formula

Every recursive function needs:

```text
Base case
Recursive case
Progress toward base case
```

## Recursion Checklist

- What is the smallest input?
- What should be returned for that input?
- What smaller problem should recursion solve?
- Does each call move closer to the base case?
- What is the recursion depth?

## Call Stack

If recursion depth is `n`, stack space is usually:

```text
O(n)
```

Recursive binary search depth:

```text
O(log n)
```

## Tail Recursion

Tail recursion means the recursive call is the last operation.

Java does not guarantee tail-call optimization, so tail recursion still usually uses stack space.

## Common Recursion Complexity

| Pattern | Time | Space |
| --- | --- | --- |
| One call with `n - 1` | `O(n)` | `O(n)` |
| One call with `n / 2` | `O(log n)` | `O(log n)` |
| Two calls with `n - 1` | `O(2^n)` | `O(n)` |
| Subsets | `O(n * 2^n)` with copying | `O(n)` excluding answer |
| Permutations | `O(n * n!)` with copying | `O(n)` excluding answer |

## Backtracking Template

```java
void backtrack(...) {
    if (baseCase) {
        answer.add(copyOfCurrent);
        return;
    }

    for (choice : choices) {
        if (!isValid(choice)) {
            continue;
        }

        choose(choice);
        backtrack(...);
        unchoose(choice);
    }
}
```

## Choose, Explore, Unchoose

```java
current.add(value);              // choose
backtrack(...);                  // explore
current.remove(current.size()-1); // unchoose
```

With `used`:

```java
used[i] = true;
current.add(nums[i]);

backtrack(...);

current.remove(current.size() - 1);
used[i] = false;
```

## Pattern Recognition

| Problem clue | Pattern |
| --- | --- |
| Choose or skip each item | Subsets or subsequences |
| All orderings | Permutations |
| Sum to target | Combination sum |
| Build valid string step by step | Generate parentheses |
| Board placement | N-Queens |
| Fill empty cells | Sudoku |
| Move in grid | Maze or word search |
| Split string into valid pieces | Palindrome partitioning or IP restore |

## Subsets

Choices:

```text
exclude current
include current
```

Base case:

```text
index == nums.length
```

## Permutations

Choices:

```text
any unused element
```

Base case:

```text
current.size() == nums.length
```

## Combination Sum

State:

```text
start index
remaining target
current combination
```

Reuse allowed:

```java
backtrack(i, remaining - candidates[i]);
```

Reuse not allowed:

```java
backtrack(i + 1, remaining - candidates[i]);
```

## Generate Parentheses

Rules:

```text
Add '(' if open < n.
Add ')' if close < open.
Stop when length == 2 * n.
```

## Grid Backtracking

Always remember:

```text
Check boundary
Check blocked or visited
Mark visited
Explore directions
Unmark visited
```

## String Partitioning

Common loop:

```java
for (int end = start; end < s.length(); end++) {
    String part = s.substring(start, end + 1);
    if (isValid(part)) {
        current.add(part);
        backtrack(end + 1);
        current.remove(current.size() - 1);
    }
}
```

## Common Bugs

- Forgetting the base case
- Forgetting to undo a choice
- Adding `current` without copying it
- Using wrong index in recursive call
- Reusing a grid cell accidentally
- Not restoring board state
- Returning too early when all answers are needed
- Not pruning invalid branches

## Interview Explanation Template

```text
The state is index plus the current path.
At each index, we make a choice and recurse.
When the base case is reached, we copy the current path into the answer.
After each recursive call, we undo the choice to try the next branch.
```

