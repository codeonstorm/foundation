# 02. Recursion With Arrays And Strings

Recursion is useful when a problem can be reduced by processing one part and then asking recursion to handle the rest.

For arrays and strings, the common recursive parameter is an index.

## Recursion With Arrays

Problem:

```text
Print all elements of an array.
```

Recursive idea:

```text
Print arr[index].
Then print everything after index.
```

Java:

```java
void printArray(int[] arr, int index) {
    if (index == arr.length) {
        return;
    }

    System.out.println(arr[index]);
    printArray(arr, index + 1);
}
```

Call:

```java
printArray(arr, 0);
```

Time complexity: `O(n)`

Space complexity: `O(n)`

## Sum Of Array

Recursive idea:

```text
sum from index = arr[index] + sum from index + 1
```

Java:

```java
int sum(int[] arr, int index) {
    if (index == arr.length) {
        return 0;
    }

    return arr[index] + sum(arr, index + 1);
}
```

Example:

```text
sum([2, 4, 6], 0)
= 2 + sum([2, 4, 6], 1)
= 2 + 4 + sum([2, 4, 6], 2)
= 2 + 4 + 6 + sum([2, 4, 6], 3)
= 12
```

## Check If Array Is Sorted

```java
boolean isSorted(int[] arr, int index) {
    if (index == arr.length - 1) {
        return true;
    }

    if (arr[index] > arr[index + 1]) {
        return false;
    }

    return isSorted(arr, index + 1);
}
```

Base case:

```text
If we reached the last element, no bad pair was found.
```

Time complexity: `O(n)`

Space complexity: `O(n)`

## Linear Search Recursively

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

Best case: `O(1)` if target is first.

Worst case: `O(n)` if target is missing or last.

Space complexity: `O(n)` because of recursion stack.

## Recursion With Strings

Strings are similar to arrays, but use:

```java
str.charAt(index)
str.length()
```

## Reverse A String Recursively

```java
String reverse(String str, int index) {
    if (index == str.length()) {
        return "";
    }

    return reverse(str, index + 1) + str.charAt(index);
}
```

Example:

```text
reverse("abc", 0)
= reverse("abc", 1) + 'a'
= reverse("abc", 2) + 'b' + 'a'
= reverse("abc", 3) + 'c' + 'b' + 'a'
= "cba"
```

Important:

String concatenation inside recursion can be expensive because Java strings are immutable.

For learning recursion, this is fine. For performance, use `StringBuilder`.

## Palindrome Check

```java
boolean isPalindrome(String str, int left, int right) {
    if (left >= right) {
        return true;
    }

    if (str.charAt(left) != str.charAt(right)) {
        return false;
    }

    return isPalindrome(str, left + 1, right - 1);
}
```

Time complexity: `O(n)`

Space complexity: `O(n)`

Only half the string is checked, but Big O drops constants.

## Remove A Character

Problem:

```text
Remove all occurrences of 'a' from a string.
```

Java:

```java
String removeA(String str, int index) {
    if (index == str.length()) {
        return "";
    }

    char current = str.charAt(index);
    String rest = removeA(str, index + 1);

    if (current == 'a') {
        return rest;
    }

    return current + rest;
}
```

Example:

```text
"banana" -> "bnn"
```

## Recursion With An Accumulator

An accumulator carries the answer while recursion moves forward.

```java
void removeA(String str, int index, StringBuilder answer) {
    if (index == str.length()) {
        return;
    }

    if (str.charAt(index) != 'a') {
        answer.append(str.charAt(index));
    }

    removeA(str, index + 1, answer);
}
```

This avoids creating many temporary strings.

## Pattern

Most array and string recursion follows this shape:

```java
returnType solve(data, index) {
    if (index == data.length) {
        return baseAnswer;
    }

    // use data[index]
    return solve(data, index + 1);
}
```

