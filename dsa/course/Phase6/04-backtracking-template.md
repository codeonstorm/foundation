# 04. Backtracking Template

Backtracking is recursion for trying choices.

The standard pattern is:

```text
choose
explore
unchoose
```

Backtracking is useful when you need to:

- Generate all valid answers
- Try many possible paths
- Stop paths that become invalid
- Build an answer step by step

## Generic Template

```java
void backtrack(State state, List<Result> answers) {
    if (isComplete(state)) {
        answers.add(buildAnswer(state));
        return;
    }

    for (Choice choice : getChoices(state)) {
        if (!isValid(choice, state)) {
            continue;
        }

        makeChoice(choice, state);
        backtrack(state, answers);
        undoChoice(choice, state);
    }
}
```

In actual Java interview code, the state is usually:

- `index`
- `current`
- `visited`
- `board`
- `answer list`

## Simple Example: Pick Or Not Pick

Problem:

```text
Generate all subsets of an array.
```

At each index, we have two choices:

- Do not include the element
- Include the element

```java
void subsets(int[] arr, int index, List<Integer> current, List<List<Integer>> answer) {
    if (index == arr.length) {
        answer.add(new ArrayList<>(current));
        return;
    }

    subsets(arr, index + 1, current, answer);

    current.add(arr[index]);
    subsets(arr, index + 1, current, answer);
    current.remove(current.size() - 1);
}
```

The last line is the backtracking step:

```java
current.remove(current.size() - 1);
```

It undoes the choice so the next path starts clean.

## Why Unchoose Matters

Suppose `current = [1]`.

If you add `2`, explore `[1, 2]`, and forget to remove `2`, then future branches incorrectly still contain `2`.

Backtracking uses shared mutable state. That makes it efficient, but it means you must restore the state after each branch.

## Another Template: Used Array

Permutations often use a `visited` or `used` array.

```java
void permutations(int[] arr, boolean[] used, List<Integer> current, List<List<Integer>> answer) {
    if (current.size() == arr.length) {
        answer.add(new ArrayList<>(current));
        return;
    }

    for (int i = 0; i < arr.length; i++) {
        if (used[i]) {
            continue;
        }

        used[i] = true;
        current.add(arr[i]);

        permutations(arr, used, current, answer);

        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

Choose:

```java
used[i] = true;
current.add(arr[i]);
```

Explore:

```java
permutations(arr, used, current, answer);
```

Unchoose:

```java
current.remove(current.size() - 1);
used[i] = false;
```

## Backtracking With Pruning

Pruning means stopping a branch early when it cannot produce a valid answer.

Example:

```java
if (sum > target) {
    return;
}
```

Pruning can make backtracking much faster in practice.

It does not always change the worst-case Big O, but it removes many useless branches.

## Backtracking Checklist

Before coding, answer these:

1. What is the current state?
2. What choices can I make from this state?
3. What makes a choice invalid?
4. What is the base case?
5. What do I add to the answer?
6. How do I undo the choice?

## Common Bugs

- Forgetting to copy `current` before adding it to `answer`
- Forgetting to remove the last choice
- Forgetting to reset `visited[i]`
- Base case too early or too late
- Allowing invalid choices
- Returning too soon when all answers are needed

