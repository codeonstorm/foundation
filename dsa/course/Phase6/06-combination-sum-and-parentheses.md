# 06. Combination Sum And Generate Parentheses

This lesson covers two classic backtracking patterns:

- Reusing choices until a target is reached
- Building only valid partial answers

## Combination Sum

Problem:

```text
Given candidates and a target, return combinations where chosen numbers sum to target.
You may reuse the same number multiple times.
```

Example:

```text
candidates = [2, 3, 6, 7], target = 7
answer = [[2, 2, 3], [7]]
```

## Key Idea

At each step:

- Choose a number.
- Reduce the remaining target.
- Stay at the same index if reuse is allowed.
- Move forward to avoid duplicate orderings.

Java:

```java
List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> answer = new ArrayList<>();
    backtrack(candidates, target, 0, new ArrayList<>(), answer);
    return answer;
}

void backtrack(int[] candidates, int remaining, int start, List<Integer> current, List<List<Integer>> answer) {
    if (remaining == 0) {
        answer.add(new ArrayList<>(current));
        return;
    }

    if (remaining < 0) {
        return;
    }

    for (int i = start; i < candidates.length; i++) {
        current.add(candidates[i]);
        backtrack(candidates, remaining - candidates[i], i, current, answer);
        current.remove(current.size() - 1);
    }
}
```

Why recursive call uses `i`:

```java
backtrack(candidates, remaining - candidates[i], i, current, answer);
```

Because the same candidate can be reused.

If reuse was not allowed, we would call with `i + 1`.

## Pruning With Sorting

```java
Arrays.sort(candidates);
```

Then:

```java
if (candidates[i] > remaining) {
    break;
}
```

Because all later numbers are even larger.

## Generate Parentheses

Problem:

```text
Given n pairs of parentheses, generate all valid combinations.
```

Example:

```text
n = 3
((()))
(()())
(())()
()(())
()()()
```

## Key Rules

We build the string one character at a time.

Allowed choices:

- Add `(` if open count is less than `n`.
- Add `)` if close count is less than open count.

Java:

```java
List<String> generateParenthesis(int n) {
    List<String> answer = new ArrayList<>();
    backtrackParentheses(n, 0, 0, new StringBuilder(), answer);
    return answer;
}

void backtrackParentheses(int n, int open, int close, StringBuilder current, List<String> answer) {
    if (current.length() == 2 * n) {
        answer.add(current.toString());
        return;
    }

    if (open < n) {
        current.append('(');
        backtrackParentheses(n, open + 1, close, current, answer);
        current.deleteCharAt(current.length() - 1);
    }

    if (close < open) {
        current.append(')');
        backtrackParentheses(n, open, close + 1, current, answer);
        current.deleteCharAt(current.length() - 1);
    }
}
```

## Why This Works

Invalid partial strings are never created.

Example invalid prefix:

```text
)(
```

This cannot happen because we only add `)` when:

```text
close < open
```

## Pattern

Use this style when:

- You build an answer step by step.
- Some partial answers are invalid.
- You can prevent invalid paths early.

## Complexity Notes

Generate parentheses has Catalan number growth.

For learning and interviews, you can say:

```text
There are Catalan(n) valid strings, each of length 2n.
Time is O(n * Catalan(n)).
Space is O(n) excluding answer storage.
```

If Catalan number is not expected, a loose upper bound is:

```text
O(2^(2n))
```

because each of `2n` positions has at most two choices before pruning.

