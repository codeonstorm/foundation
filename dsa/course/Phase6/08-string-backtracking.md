# 08. String Backtracking

String backtracking problems build a string or partition a string step by step.

Common state:

- Current index
- Current built string
- Current list of parts
- Final answer list

## Letter Combinations Of Phone Number

Problem:

```text
Given digits from 2 to 9, return all letter combinations they can represent.
```

Mapping:

```text
2 -> abc
3 -> def
4 -> ghi
5 -> jkl
6 -> mno
7 -> pqrs
8 -> tuv
9 -> wxyz
```

Java:

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

    backtrackLetters(digits, 0, map, new StringBuilder(), answer);
    return answer;
}

void backtrackLetters(String digits, int index, String[] map, StringBuilder current, List<String> answer) {
    if (index == digits.length()) {
        answer.add(current.toString());
        return;
    }

    int digit = digits.charAt(index) - '0';
    String letters = map[digit];

    for (int i = 0; i < letters.length(); i++) {
        current.append(letters.charAt(i));
        backtrackLetters(digits, index + 1, map, current, answer);
        current.deleteCharAt(current.length() - 1);
    }
}
```

If every digit has up to 4 letters and there are `n` digits:

```text
Time: O(n * 4^n)
Space: O(n) excluding answer storage
```

## Palindrome Partitioning

Problem:

```text
Partition a string so every part is a palindrome.
```

Example:

```text
"aab" -> [["a", "a", "b"], ["aa", "b"]]
```

Strategy:

- Start at index `0`.
- Try every possible ending index.
- If substring is palindrome, choose it.
- Recurse from the next index.
- Remove the chosen substring.

Java:

```java
List<List<String>> partition(String s) {
    List<List<String>> answer = new ArrayList<>();
    backtrackPartition(s, 0, new ArrayList<>(), answer);
    return answer;
}

void backtrackPartition(String s, int start, List<String> current, List<List<String>> answer) {
    if (start == s.length()) {
        answer.add(new ArrayList<>(current));
        return;
    }

    for (int end = start; end < s.length(); end++) {
        if (isPalindrome(s, start, end)) {
            current.add(s.substring(start, end + 1));
            backtrackPartition(s, end + 1, current, answer);
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

Complexity is often described as:

```text
Time: O(n * 2^n)
Space: O(n) recursion depth, excluding answer storage
```

The palindrome checks and substring creation can add extra cost depending on implementation.

## Restore IP Addresses

Problem:

```text
Given a string of digits, return all possible valid IP addresses.
```

Valid IP address:

- Exactly 4 parts
- Each part is between `0` and `255`
- No leading zero unless the part is exactly `"0"`

Example:

```text
"25525511135" -> ["255.255.11.135", "255.255.111.35"]
```

Java:

```java
List<String> restoreIpAddresses(String s) {
    List<String> answer = new ArrayList<>();
    backtrackIp(s, 0, new ArrayList<>(), answer);
    return answer;
}

void backtrackIp(String s, int index, List<String> parts, List<String> answer) {
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
        backtrackIp(s, index + len, parts, answer);
        parts.remove(parts.size() - 1);
    }
}

boolean isValidIpPart(String part) {
    if (part.length() > 1 && part.charAt(0) == '0') {
        return false;
    }

    int value = Integer.parseInt(part);
    return value <= 255;
}
```

Because IP addresses have only 4 parts and each part has max length 3, the search space is small.

## String Backtracking Checklist

- What index am I at?
- What substring or character can I choose now?
- Is this partial choice valid?
- What do I append to the current answer?
- How do I undo it?
- When do I add to the final answer?

## Common Mistakes

- Not handling empty input
- Allowing leading zeroes in IP addresses
- Forgetting to copy `current`
- Using `substring` bounds incorrectly
- Adding invalid partial strings and trying to fix them later

