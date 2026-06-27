import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BitManipulationExamples {
    public static void main(String[] args) {
        int num = 13; // 1101

        System.out.println("Binary of 13: " + Integer.toBinaryString(num));
        System.out.println("Is bit 2 set in 13: " + isSet(num, 2));
        System.out.println("Set bit 1 in 13: " + setBit(num, 1));
        System.out.println("Clear bit 2 in 13: " + clearBit(num, 2));
        System.out.println("Toggle bit 0 in 13: " + toggleBit(num, 0));
        System.out.println("Update bit 1 to 1 in 13: " + updateBit(num, 1, 1));
        System.out.println("Count set bits in 13: " + countSetBits(num));
        System.out.println("Is 16 power of two: " + isPowerOfTwo(16));
        System.out.println("Counting bits up to 5: " + Arrays.toString(countBits(5)));
        System.out.println("Hamming distance 1 and 4: " + hammingDistance(1, 4));

        System.out.println("Single number: " + singleNumber(new int[]{4, 1, 2, 1, 2}));
        System.out.println("Missing number: " + missingNumber(new int[]{3, 0, 1}));
        System.out.println("Two single numbers: " + Arrays.toString(twoSingleNumbers(new int[]{1, 2, 1, 3, 2, 5})));
        System.out.println("Single among triples: " + singleNumberAmongTriples(new int[]{2, 2, 3, 2}));

        System.out.println("Subsets of [1, 2, 3]: " + subsets(new int[]{1, 2, 3}));
        System.out.println("Subset sums of [1, 2, 3]: " + subsetSums(new int[]{1, 2, 3}));
        System.out.println("Maximum XOR: " + findMaximumXOR(new int[]{3, 10, 5, 25, 2, 8}));

        int[][] cost = {
                {9, 2, 7},
                {6, 4, 3},
                {5, 8, 1}
        };
        System.out.println("Minimum assignment cost: " + minAssignmentCost(cost));
    }

    // Time: O(1), Space: O(1)
    static boolean isSet(int num, int bit) {
        return (num & (1 << bit)) != 0;
    }

    // Time: O(1), Space: O(1)
    static int setBit(int num, int bit) {
        return num | (1 << bit);
    }

    // Time: O(1), Space: O(1)
    static int clearBit(int num, int bit) {
        return num & ~(1 << bit);
    }

    // Time: O(1), Space: O(1)
    static int toggleBit(int num, int bit) {
        return num ^ (1 << bit);
    }

    // Time: O(1), Space: O(1)
    static int updateBit(int num, int bit, int value) {
        num = clearBit(num, bit);
        return num | (value << bit);
    }

    // Time: O(number of set bits), Space: O(1)
    static int countSetBits(int num) {
        int count = 0;

        while (num != 0) {
            num &= (num - 1);
            count++;
        }

        return count;
    }

    // Time: O(1), Space: O(1)
    static boolean isPowerOfTwo(int num) {
        return num > 0 && (num & (num - 1)) == 0;
    }

    // Time: O(n), Space: O(n)
    static int[] countBits(int n) {
        int[] bits = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            bits[i] = bits[i >> 1] + (i & 1);
        }

        return bits;
    }

    // Time: O(number of set bits), Space: O(1)
    static int hammingDistance(int x, int y) {
        return countSetBits(x ^ y);
    }

    // Time: O(n), Space: O(1)
    static int singleNumber(int[] nums) {
        int answer = 0;

        for (int num : nums) {
            answer ^= num;
        }

        return answer;
    }

    // Time: O(n), Space: O(1)
    static int missingNumber(int[] nums) {
        int answer = nums.length;

        for (int i = 0; i < nums.length; i++) {
            answer ^= i;
            answer ^= nums[i];
        }

        return answer;
    }

    // Time: O(n), Space: O(1)
    static int[] twoSingleNumbers(int[] nums) {
        int xor = 0;

        for (int num : nums) {
            xor ^= num;
        }

        int diffBit = xor & -xor;
        int first = 0;
        int second = 0;

        for (int num : nums) {
            if ((num & diffBit) == 0) {
                first ^= num;
            } else {
                second ^= num;
            }
        }

        return new int[]{first, second};
    }

    // Time: O(32 * n), Space: O(1)
    static int singleNumberAmongTriples(int[] nums) {
        int answer = 0;

        for (int bit = 0; bit < 32; bit++) {
            int count = 0;

            for (int num : nums) {
                if ((num & (1 << bit)) != 0) {
                    count++;
                }
            }

            if (count % 3 != 0) {
                answer |= (1 << bit);
            }
        }

        return answer;
    }

    // Time: O(n * 2^n), Space: O(n * 2^n) including answer
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> answer = new ArrayList<>();
        int totalMasks = 1 << nums.length;

        for (int mask = 0; mask < totalMasks; mask++) {
            List<Integer> current = new ArrayList<>();

            for (int bit = 0; bit < nums.length; bit++) {
                if ((mask & (1 << bit)) != 0) {
                    current.add(nums[bit]);
                }
            }

            answer.add(current);
        }

        return answer;
    }

    // Time: O(n * 2^n), Space: O(2^n)
    static List<Integer> subsetSums(int[] nums) {
        List<Integer> answer = new ArrayList<>();
        int totalMasks = 1 << nums.length;

        for (int mask = 0; mask < totalMasks; mask++) {
            int sum = 0;

            for (int bit = 0; bit < nums.length; bit++) {
                if ((mask & (1 << bit)) != 0) {
                    sum += nums[bit];
                }
            }

            answer.add(sum);
        }

        return answer;
    }

    // Time: O(32 * n), Space: O(n)
    static int findMaximumXOR(int[] nums) {
        int max = 0;
        int mask = 0;

        for (int bit = 30; bit >= 0; bit--) {
            mask |= (1 << bit);
            Set<Integer> prefixes = new HashSet<>();

            for (int num : nums) {
                prefixes.add(num & mask);
            }

            int candidate = max | (1 << bit);

            for (int prefix : prefixes) {
                if (prefixes.contains(prefix ^ candidate)) {
                    max = candidate;
                    break;
                }
            }
        }

        return max;
    }

    // Time: O(n * 2^n), Space: O(2^n)
    static int minAssignmentCost(int[][] cost) {
        int n = cost.length;
        int totalMasks = 1 << n;
        int[] dp = new int[totalMasks];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;

        for (int mask = 0; mask < totalMasks; mask++) {
            int person = Integer.bitCount(mask);

            if (person == n) {
                continue;
            }

            for (int task = 0; task < n; task++) {
                if ((mask & (1 << task)) == 0) {
                    int nextMask = mask | (1 << task);
                    dp[nextMask] = Math.min(dp[nextMask], dp[mask] + cost[person][task]);
                }
            }
        }

        return dp[totalMasks - 1];
    }
}

