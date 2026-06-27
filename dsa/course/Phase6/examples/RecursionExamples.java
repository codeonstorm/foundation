import java.util.Arrays;

public class RecursionExamples {
    public static void main(String[] args) {
        int[] nums = {2, 4, 6, 8};

        System.out.print("Print decreasing from 5: ");
        printDecreasing(5);
        System.out.println();

        System.out.print("Print increasing to 5: ");
        printIncreasing(5);
        System.out.println();

        System.out.println("Factorial 5: " + factorial(5));
        System.out.println("Sum of " + Arrays.toString(nums) + ": " + sum(nums, 0));
        System.out.println("Is sorted: " + isSorted(nums, 0));
        System.out.println("Search 6: " + search(nums, 0, 6));
        System.out.println("Reverse 'recursion': " + reverse("recursion", 0));
        System.out.println("Is 'madam' palindrome: " + isPalindrome("madam", 0, 4));
        System.out.println("Naive fibonacci 6: " + fib(6));
        System.out.println("Memo fibonacci 20: " + fibMemo(20));
    }

    // Time: O(n), Space: O(n)
    static void printDecreasing(int n) {
        if (n == 0) {
            return;
        }

        System.out.print(n + " ");
        printDecreasing(n - 1);
    }

    // Time: O(n), Space: O(n)
    static void printIncreasing(int n) {
        if (n == 0) {
            return;
        }

        printIncreasing(n - 1);
        System.out.print(n + " ");
    }

    // Time: O(n), Space: O(n)
    static int factorial(int n) {
        if (n <= 1) {
            return 1;
        }

        return n * factorial(n - 1);
    }

    // Time: O(n), Space: O(n)
    static int sum(int[] arr, int index) {
        if (index == arr.length) {
            return 0;
        }

        return arr[index] + sum(arr, index + 1);
    }

    // Time: O(n), Space: O(n)
    static boolean isSorted(int[] arr, int index) {
        if (arr.length <= 1 || index == arr.length - 1) {
            return true;
        }

        if (arr[index] > arr[index + 1]) {
            return false;
        }

        return isSorted(arr, index + 1);
    }

    // Time: O(n), Space: O(n)
    static int search(int[] arr, int index, int target) {
        if (index == arr.length) {
            return -1;
        }

        if (arr[index] == target) {
            return index;
        }

        return search(arr, index + 1, target);
    }

    // Simple learning version. In Java, repeated string concatenation adds extra cost.
    static String reverse(String text, int index) {
        if (index == text.length()) {
            return "";
        }

        return reverse(text, index + 1) + text.charAt(index);
    }

    // Time: O(n), Space: O(n)
    static boolean isPalindrome(String text, int left, int right) {
        if (left >= right) {
            return true;
        }

        if (text.charAt(left) != text.charAt(right)) {
            return false;
        }

        return isPalindrome(text, left + 1, right - 1);
    }

    // Time: O(2^n), Space: O(n)
    static int fib(int n) {
        if (n <= 1) {
            return n;
        }

        return fib(n - 1) + fib(n - 2);
    }

    // Time: O(n), Space: O(n)
    static int fibMemo(int n) {
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return fibMemo(n, memo);
    }

    static int fibMemo(int n, int[] memo) {
        if (n <= 1) {
            return n;
        }

        if (memo[n] != -1) {
            return memo[n];
        }

        memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        return memo[n];
    }
}

