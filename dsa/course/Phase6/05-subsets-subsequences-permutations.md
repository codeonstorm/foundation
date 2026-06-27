# 05. Subsets, Subsequences, And Permutations

These are the core generation patterns in recursion and backtracking.

If you understand these three, many harder problems become combinations of the same ideas.

## Subsets

Problem:

```text
Given [1, 2, 3], generate all subsets.
```

Expected output:

```text
[]
[3]
[2]
[2, 3]
[1]
[1, 3]
[1, 2]
[1, 2, 3]
```

Order may vary.

## Subsets: Pick Or Not Pick

At every index:

- Exclude the current element
- Include the current element

Java:

```java
List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> answer = new ArrayList<>();
    backtrackSubsets(nums, 0, new ArrayList<>(), answer);
    return answer;
}

void backtrackSubsets(int[] nums, int index, List<Integer> current, List<List<Integer>> answer) {
    if (index == nums.length) {
        answer.add(new ArrayList<>(current));
        return;
    }

    backtrackSubsets(nums, index + 1, current, answer);

    current.add(nums[index]);
    backtrackSubsets(nums, index + 1, current, answer);
    current.remove(current.size() - 1);
}
```

Complexity:

```text
Time: O(n * 2^n)
Space: O(n) recursion stack, excluding answer storage
```

Why `n * 2^n`?

There are `2^n` subsets, and copying each subset can take up to `n` time.

## Subsets: For Loop Style

Another common style:

```java
void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> answer) {
    answer.add(new ArrayList<>(current));

    for (int i = start; i < nums.length; i++) {
        current.add(nums[i]);
        backtrack(nums, i + 1, current, answer);
        current.remove(current.size() - 1);
    }
}
```

This style is useful for combinations.

## Subsequences

A subsequence keeps relative order but may skip elements.

For string `"abc"`, subsequences include:

```text
""
"a"
"b"
"c"
"ab"
"ac"
"bc"
"abc"
```

Subsequence generation is almost the same as subset generation.

```java
void subsequences(String str, int index, StringBuilder current, List<String> answer) {
    if (index == str.length()) {
        answer.add(current.toString());
        return;
    }

    subsequences(str, index + 1, current, answer);

    current.append(str.charAt(index));
    subsequences(str, index + 1, current, answer);
    current.deleteCharAt(current.length() - 1);
}
```

Time complexity: `O(n * 2^n)`

Space complexity: `O(n)` excluding answer storage.

## Permutations

A permutation is an ordering of all elements.

For `[1, 2, 3]`, examples:

```text
[1, 2, 3]
[1, 3, 2]
[2, 1, 3]
[2, 3, 1]
[3, 1, 2]
[3, 2, 1]
```

## Permutations With Used Array

```java
List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> answer = new ArrayList<>();
    boolean[] used = new boolean[nums.length];
    backtrackPermutations(nums, used, new ArrayList<>(), answer);
    return answer;
}

void backtrackPermutations(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> answer) {
    if (current.size() == nums.length) {
        answer.add(new ArrayList<>(current));
        return;
    }

    for (int i = 0; i < nums.length; i++) {
        if (used[i]) {
            continue;
        }

        used[i] = true;
        current.add(nums[i]);

        backtrackPermutations(nums, used, current, answer);

        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

Complexity:

```text
Time: O(n * n!)
Space: O(n) recursion stack plus O(n) used array, excluding answer storage
```

## Permutations By Swapping

This version modifies the array directly.

```java
void permuteBySwap(int[] nums, int index, List<List<Integer>> answer) {
    if (index == nums.length) {
        List<Integer> current = new ArrayList<>();
        for (int num : nums) {
            current.add(num);
        }
        answer.add(current);
        return;
    }

    for (int i = index; i < nums.length; i++) {
        swap(nums, index, i);
        permuteBySwap(nums, index + 1, answer);
        swap(nums, index, i);
    }
}

void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

This is also backtracking:

```text
choose: swap
explore: recursive call
unchoose: swap back
```

## Subsets vs Subsequences vs Permutations

| Pattern | Keeps order? | Uses all elements? | Count |
| --- | --- | --- | --- |
| Subsets | Usually yes in generated list | No | `2^n` |
| Subsequences | Yes | No | `2^n` |
| Permutations | No fixed original order | Yes | `n!` |

## How To Recognize The Pattern

Use subsets or subsequences when:

- Each item can be chosen or skipped.
- You need all groups or all possible selections.

Use permutations when:

- You need all orderings.
- Every element must appear once.
- The position matters.

