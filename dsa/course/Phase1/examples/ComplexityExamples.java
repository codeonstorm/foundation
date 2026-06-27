import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class ComplexityExamples {
    public static void main(String[] args) {
        int[] arr = {4, 1, 7, 1, 9, 3};
        int[] sorted = {1, 2, 4, 7, 9, 11};

        System.out.println("First element: " + first(arr));
        System.out.println("Sum: " + sum(arr));
        System.out.println("Max: " + max(arr));
        System.out.println("Binary search index of 7: " + binarySearch(sorted, 7));
        System.out.println("Has duplicate using HashSet: " + hasDuplicateHashSet(arr));
        System.out.println("Has duplicate using sort: " + hasDuplicateSort(arr.clone()));
        System.out.println("Pair sum 13 in sorted array: " + hasPairSum(sorted, 13));
        System.out.println("Fibonacci memo n=10: " + fibMemo(10));
        System.out.println("Kth largest k=2: " + kthLargest(arr, 2));

        Map<Integer, Integer> frequency = frequencyMap(arr);
        System.out.println("Frequency map: " + frequency);
    }

    // Time: O(1), Space: O(1)
    static int first(int[] arr) {
        return arr[0];
    }

    // Time: O(n), Space: O(1)
    static int sum(int[] arr) {
        int total = 0;

        for (int num : arr) {
            total += num;
        }

        return total;
    }

    // Time: O(n), Space: O(1)
    static int max(int[] arr) {
        int answer = arr[0];

        for (int num : arr) {
            answer = Math.max(answer, num);
        }

        return answer;
    }

    // Time: O(log n), Space: O(1)
    static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            }

            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    // Time: O(n) average, Space: O(n)
    static boolean hasDuplicateHashSet(int[] arr) {
        Set<Integer> seen = new HashSet<>();

        for (int num : arr) {
            if (seen.contains(num)) {
                return true;
            }

            seen.add(num);
        }

        return false;
    }

    // Time: O(n log n), Space: depends on sorting implementation
    static boolean hasDuplicateSort(int[] arr) {
        Arrays.sort(arr);

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                return true;
            }
        }

        return false;
    }

    // Time: O(n), Space: O(1)
    static boolean hasPairSum(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int sum = arr[left] + arr[right];

            if (sum == target) {
                return true;
            }

            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return false;
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

    // Time: O(n), Space: O(n)
    static Map<Integer, Integer> frequencyMap(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        return map;
    }

    // Time: O(n log k), Space: O(k)
    static int kthLargest(int[] arr, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : arr) {
            minHeap.add(num);

            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        return minHeap.peek();
    }
}

