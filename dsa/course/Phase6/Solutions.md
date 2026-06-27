# Phase 6 Practice Solutions

Use this after attempting `Practice.md`.

## 1. Print Decreasing

```java
void printDecreasing(int n) {
    if (n == 0) {
        return;
    }

    System.out.println(n);
    printDecreasing(n - 1);
}
```

Time: `O(n)`

Space: `O(n)`

## 2. Print Increasing

```java
void printIncreasing(int n) {
    if (n == 0) {
        return;
    }

    printIncreasing(n - 1);
    System.out.println(n);
}
```

The print happens after the recursive call, so output appears while calls return.

Time: `O(n)`

Space: `O(n)`

## 3. Factorial

```java
int factorial(int n) {
    if (n <= 1) {
        return 1;
    }

    return n * factorial(n - 1);
}
```

Time: `O(n)`

Space: `O(n)`

## 4. Sum Of Array

```java
int sum(int[] arr, int index) {
    if (index == arr.length) {
        return 0;
    }

    return arr[index] + sum(arr, index + 1);
}
```

State: `index`

Base case: reached the end of the array.

Time: `O(n)`

Space: `O(n)`

## 5. Check Sorted Array

```java
boolean isSorted(int[] arr, int index) {
    if (arr.length <= 1 || index == arr.length - 1) {
        return true;
    }

    if (arr[index] > arr[index + 1]) {
        return false;
    }

    return isSorted(arr, index + 1);
}
```

Time: `O(n)`

Space: `O(n)`

## 6. Recursive Linear Search

```java
int search(int[] arr, int index, int target) {
    if (index == arr.length) {
        return -1;
    }

    if (arr[index] == target) {
        return index;
    }

    return search(arr, index + 1, target);
}
```

Best case: `O(1)`

Worst case: `O(n)`

Space: `O(n)`

## 7. Reverse String

```java
String reverse(String s, int index) {
    if (index == s.length()) {
        return "";
    }

    return reverse(s, index + 1) + s.charAt(index);
}
```

Time: `O(n^2)` in Java because repeated string concatenation creates new strings.

Space: `O(n)` stack, plus temporary strings.

Better version:

```java
void reverse(String s, int index, StringBuilder answer) {
    if (index < 0) {
        return;
    }

    answer.append(s.charAt(index));
    reverse(s, index - 1, answer);
}
```

## 8. Palindrome String

```java
boolean isPalindrome(String s, int left, int right) {
    if (left >= right) {
        return true;
    }

    if (s.charAt(left) != s.charAt(right)) {
        return false;
    }

    return isPalindrome(s, left + 1, right - 1);
}
```

Time: `O(n)`

Space: `O(n)`

## 9. Analyze Fibonacci

Time: `O(2^n)`

Space: `O(n)`

Reason: Each call branches into two more calls, but the deepest active path has length `n`.

## 10. Analyze Subset Shape

Time: `O(2^n)`

Space: `O(n)`

Reason: Each index creates two branches. Maximum recursion depth is `n`.

## 11. Generate Subsets

```java
List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> answer = new ArrayList<>();
    subsetHelper(nums, 0, new ArrayList<>(), answer);
    return answer;
}

void subsetHelper(int[] nums, int index, List<Integer> current, List<List<Integer>> answer) {
    if (index == nums.length) {
        answer.add(new ArrayList<>(current));
        return;
    }

    subsetHelper(nums, index + 1, current, answer);

    current.add(nums[index]);
    subsetHelper(nums, index + 1, current, answer);
    current.remove(current.size() - 1);
}
```

Time: `O(n * 2^n)`

Space: `O(n)` excluding answer storage.

## 12. Generate Subsequences Of A String

```java
List<String> subsequences(String s) {
    List<String> answer = new ArrayList<>();
    subsequenceHelper(s, 0, new StringBuilder(), answer);
    return answer;
}

void subsequenceHelper(String s, int index, StringBuilder current, List<String> answer) {
    if (index == s.length()) {
        answer.add(current.toString());
        return;
    }

    subsequenceHelper(s, index + 1, current, answer);

    current.append(s.charAt(index));
    subsequenceHelper(s, index + 1, current, answer);
    current.deleteCharAt(current.length() - 1);
}
```

Time: `O(n * 2^n)`

Space: `O(n)` excluding answer storage.

## 13. Generate Permutations

```java
List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> answer = new ArrayList<>();
    boolean[] used = new boolean[nums.length];
    permutationHelper(nums, used, new ArrayList<>(), answer);
    return answer;
}

void permutationHelper(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> answer) {
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

        permutationHelper(nums, used, current, answer);

        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

Time: `O(n * n!)`

Space: `O(n)` excluding answer storage.

## 14. Combination Sum

```java
List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> answer = new ArrayList<>();
    Arrays.sort(candidates);
    combinationHelper(candidates, target, 0, new ArrayList<>(), answer);
    return answer;
}

void combinationHelper(int[] candidates, int remaining, int start, List<Integer> current, List<List<Integer>> answer) {
    if (remaining == 0) {
        answer.add(new ArrayList<>(current));
        return;
    }

    for (int i = start; i < candidates.length; i++) {
        if (candidates[i] > remaining) {
            break;
        }

        current.add(candidates[i]);
        combinationHelper(candidates, remaining - candidates[i], i, current, answer);
        current.remove(current.size() - 1);
    }
}
```

The recursive call uses `i` because the same number can be reused.

## 15. Generate Parentheses

```java
List<String> generateParenthesis(int n) {
    List<String> answer = new ArrayList<>();
    parenthesisHelper(n, 0, 0, new StringBuilder(), answer);
    return answer;
}

void parenthesisHelper(int n, int open, int close, StringBuilder current, List<String> answer) {
    if (current.length() == 2 * n) {
        answer.add(current.toString());
        return;
    }

    if (open < n) {
        current.append('(');
        parenthesisHelper(n, open + 1, close, current, answer);
        current.deleteCharAt(current.length() - 1);
    }

    if (close < open) {
        current.append(')');
        parenthesisHelper(n, open, close + 1, current, answer);
        current.deleteCharAt(current.length() - 1);
    }
}
```

Only valid prefixes are created.

## 16. N-Queens

Key idea:

```text
Place one queen per row.
Try every column.
Only recurse if the position is safe.
```

See `07-board-and-grid-backtracking.md` and `examples/BacktrackingExamples.java` for a complete implementation.

## 17. Sudoku Solver

Key idea:

```text
Find the first empty cell.
Try digits 1 to 9.
If a digit works, recurse.
If recursion fails, undo the digit.
```

The function returns `true` when the board is solved.

See `07-board-and-grid-backtracking.md` for full code.

## 18. Rat In A Maze

Key idea:

```text
Mark current cell visited.
Try D, L, R, U.
Unmark current cell before returning.
```

Do not revisit cells in the same path.

## 19. Word Search

Key idea:

```text
Match the current character.
Temporarily mark the cell visited.
Search four directions for the next character.
Restore the cell.
```

Time is commonly described as `O(rows * cols * 4^wordLength)`.

## 20. Letter Combinations

```java
List<String> letterCombinations(String digits) {
    List<String> answer = new ArrayList<>();

    if (digits.length() == 0) {
        return answer;
    }

    String[] map = {
            "", "", "abc", "def", "ghi",
            "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    letterHelper(digits, 0, map, new StringBuilder(), answer);
    return answer;
}

void letterHelper(String digits, int index, String[] map, StringBuilder current, List<String> answer) {
    if (index == digits.length()) {
        answer.add(current.toString());
        return;
    }

    String letters = map[digits.charAt(index) - '0'];

    for (int i = 0; i < letters.length(); i++) {
        current.append(letters.charAt(i));
        letterHelper(digits, index + 1, map, current, answer);
        current.deleteCharAt(current.length() - 1);
    }
}
```

Time: `O(n * 4^n)`

Space: `O(n)` excluding answer storage.

## 21. Palindrome Partitioning

```java
List<List<String>> partition(String s) {
    List<List<String>> answer = new ArrayList<>();
    partitionHelper(s, 0, new ArrayList<>(), answer);
    return answer;
}

void partitionHelper(String s, int start, List<String> current, List<List<String>> answer) {
    if (start == s.length()) {
        answer.add(new ArrayList<>(current));
        return;
    }

    for (int end = start; end < s.length(); end++) {
        if (isPalindrome(s, start, end)) {
            current.add(s.substring(start, end + 1));
            partitionHelper(s, end + 1, current, answer);
            current.remove(current.size() - 1);
        }
    }
}

boolean isPalindrome(String s, int left, int right) {
    while (left < right) {
        if (s.charAt(left) != s.charAt(right)) {
            return false;
        }

        left++;
        right--;
    }

    return true;
}
```

## 22. Restore IP Addresses

```java
List<String> restoreIpAddresses(String s) {
    List<String> answer = new ArrayList<>();
    ipHelper(s, 0, new ArrayList<>(), answer);
    return answer;
}

void ipHelper(String s, int index, List<String> parts, List<String> answer) {
    if (parts.size() == 4) {
        if (index == s.length()) {
            answer.add(String.join(".", parts));
        }
        return;
    }

    for (int len = 1; len <= 3; len++) {
        if (index + len > s.length()) {
            break;
        }

        String part = s.substring(index, index + len);

        if (!isValidIpPart(part)) {
            continue;
        }

        parts.add(part);
        ipHelper(s, index + len, parts, answer);
        parts.remove(parts.size() - 1);
    }
}

boolean isValidIpPart(String part) {
    if (part.length() > 1 && part.charAt(0) == '0') {
        return false;
    }

    return Integer.parseInt(part) <= 255;
}
```

Main validation rules:

- Exactly 4 parts
- Use all characters
- Each part is at most 255
- No leading zeroes

