# 07. String DP

String DP usually uses two indices.

Common state:

```text
dp[i][j] = answer using first i characters of string A and first j characters of string B
```

Or:

```text
dp[left][right] = answer for substring from left to right
```

## Longest Common Subsequence

Problem:

```text
Given two strings, find the length of their longest common subsequence.
```

A subsequence keeps order but may skip characters.

Example:

```text
"abcde" and "ace" -> "ace" length 3
```

State:

```text
dp[i][j] = LCS length using first i chars of text1 and first j chars of text2
```

Transition:

If characters match:

```text
dp[i][j] = 1 + dp[i - 1][j - 1]
```

If not:

```text
dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
```

Java:

```java
int longestCommonSubsequence(String a, String b) {
    int n = a.length();
    int m = b.length();
    int[][] dp = new int[n + 1][m + 1];

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                dp[i][j] = 1 + dp[i - 1][j - 1];
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }

    return dp[n][m];
}
```

Time: `O(n * m)`

Space: `O(n * m)`

## LCS Space Optimized

Each row only needs the previous row.

```java
int lcsOptimized(String a, String b) {
    int n = a.length();
    int m = b.length();
    int[] prev = new int[m + 1];
    int[] curr = new int[m + 1];

    for (int i = 1; i <= n; i++) {
        Arrays.fill(curr, 0);

        for (int j = 1; j <= m; j++) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                curr[j] = 1 + prev[j - 1];
            } else {
                curr[j] = Math.max(prev[j], curr[j - 1]);
            }
        }

        int[] temp = prev;
        prev = curr;
        curr = temp;
    }

    return prev[m];
}
```

Space: `O(m)`

## Edit Distance

Problem:

```text
Convert word1 to word2 using insert, delete, and replace.
Find minimum operations.
```

State:

```text
dp[i][j] = minimum operations to convert first i chars of word1 to first j chars of word2
```

Base cases:

```text
dp[i][0] = i
dp[0][j] = j
```

Transition:

If characters match:

```text
dp[i][j] = dp[i - 1][j - 1]
```

Otherwise:

```text
insert = dp[i][j - 1]
delete = dp[i - 1][j]
replace = dp[i - 1][j - 1]
dp[i][j] = 1 + min(insert, delete, replace)
```

Java:

```java
int minDistance(String word1, String word2) {
    int n = word1.length();
    int m = word2.length();
    int[][] dp = new int[n + 1][m + 1];

    for (int i = 0; i <= n; i++) {
        dp[i][0] = i;
    }

    for (int j = 0; j <= m; j++) {
        dp[0][j] = j;
    }

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                int insert = dp[i][j - 1];
                int delete = dp[i - 1][j];
                int replace = dp[i - 1][j - 1];
                dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
            }
        }
    }

    return dp[n][m];
}
```

Time: `O(n * m)`

Space: `O(n * m)`

## Palindromic Substrings

Problem:

```text
Count all palindromic substrings in a string.
```

State:

```text
dp[left][right] = true if substring left..right is palindrome
```

Transition:

```text
s[left] == s[right] and inside substring is palindrome
```

Java:

```java
int countPalindromicSubstrings(String s) {
    int n = s.length();
    boolean[][] dp = new boolean[n][n];
    int count = 0;

    for (int length = 1; length <= n; length++) {
        for (int left = 0; left + length - 1 < n; left++) {
            int right = left + length - 1;

            if (s.charAt(left) == s.charAt(right)) {
                if (length <= 2 || dp[left + 1][right - 1]) {
                    dp[left][right] = true;
                    count++;
                }
            }
        }
    }

    return count;
}
```

Time: `O(n^2)`

Space: `O(n^2)`

## Palindromic Substrings By Expanding Center

This is not table DP, but it is often simpler.

```java
int countPalindromicSubstringsCenter(String s) {
    int count = 0;

    for (int center = 0; center < s.length(); center++) {
        count += expand(s, center, center);
        count += expand(s, center, center + 1);
    }

    return count;
}

int expand(String s, int left, int right) {
    int count = 0;

    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
        count++;
        left--;
        right++;
    }

    return count;
}
```

Time: `O(n^2)`

Space: `O(1)`

## Longest Palindromic Subsequence

State:

```text
dp[left][right] = length of longest palindromic subsequence in substring left..right
```

Transition:

```text
if s[left] == s[right]:
    dp[left][right] = 2 + dp[left + 1][right - 1]
else:
    dp[left][right] = max(dp[left + 1][right], dp[left][right - 1])
```

This pattern is useful because many substring DP problems fill by increasing length.

## How To Recognize String DP

Use string DP when:

- You compare two strings.
- You transform one string into another.
- You find a longest common pattern.
- You split a string into substrings.
- You need palindrome information over intervals.

## String DP Checklist

- Are there one or two strings?
- Does order matter?
- Is this subsequence or substring?
- What do `i` and `j` represent?
- Should the table be prefix-based or interval-based?
- What are the empty-string base cases?

