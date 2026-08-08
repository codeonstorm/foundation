import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathForDsaExamples {
    static final long MOD = 1_000_000_007L;

    public static void main(String[] args) {
        System.out.println("Is 29 prime: " + isPrime(29));
        System.out.println("Factors of 36: " + factors(36));
        System.out.println("Count primes less than 20: " + countPrimes(20));
        System.out.println("List primes less than 20: " + listPrimes(20));
        System.out.println("GCD of 48 and 18: " + gcd(48, 18));
        System.out.println("LCM of 12 and 18: " + lcm(12, 18));
        System.out.println("GCD of array: " + gcdArray(new int[]{24, 36, 60}));
        System.out.println("Normalize -3 mod 5: " + normalizeMod(-3, 5));
        System.out.println("2^10 mod MOD: " + modPow(2, 10, MOD));
        System.out.println("myPow(2.0, -3): " + myPow(2.0, -3));
        System.out.println("5! mod MOD: " + factorialMod(5, MOD));
        System.out.println("5P2: " + nPr(5, 2));
        System.out.println("5C2: " + nCr(5, 2));
        System.out.println("Inverse of 2 mod MOD: " + modInverse(2, MOD));

        CombinationMod combination = new CombinationMod(100, MOD);
        System.out.println("10C3 mod MOD: " + combination.nCr(10, 3));

        System.out.println("Trailing zeroes in 100!: " + trailingZeroes(100));
        System.out.println("Is 19 happy: " + isHappy(19));
        System.out.println("Is 2 happy: " + isHappy(2));
        System.out.println("Excel column AB: " + titleToNumber("AB"));
    }

    // Time: O(sqrt(n)), Space: O(1)
    static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        for (int i = 2; i <= n / i; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    // Time: O(sqrt(n)), Space: O(number of factors)
    static List<Integer> factors(int n) {
        List<Integer> answer = new ArrayList<>();

        for (int i = 1; i <= n / i; i++) {
            if (n % i == 0) {
                answer.add(i);

                if (i != n / i) {
                    answer.add(n / i);
                }
            }
        }

        return answer;
    }

    // Time: O(n log log n), Space: O(n)
    static int countPrimes(int n) {
        if (n <= 2) {
            return 0;
        }

        boolean[] isPrime = sieve(n);
        int count = 0;

        for (boolean prime : isPrime) {
            if (prime) {
                count++;
            }
        }

        return count;
    }

    // Time: O(n log log n), Space: O(n)
    static List<Integer> listPrimes(int n) {
        List<Integer> primes = new ArrayList<>();

        if (n <= 2) {
            return primes;
        }

        boolean[] isPrime = sieve(n);

        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes;
    }

    static boolean[] sieve(int n) {
        boolean[] isPrime = new boolean[n];

        if (n <= 2) {
            return isPrime;
        }

        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        for (int p = 2; p <= (n - 1) / p; p++) {
            if (isPrime[p]) {
                for (int multiple = p * p; multiple < n; multiple += p) {
                    isPrime[multiple] = false;
                }
            }
        }

        return isPrime;
    }

    // Time: O(log min(a, b)), Space: O(1)
    static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    static long lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        return Math.abs((long) a / gcd(a, b) * b);
    }

    static int gcdArray(int[] nums) {
        int answer = 0;

        for (int num : nums) {
            answer = gcd(answer, num);
        }

        return answer;
    }

    static int normalizeMod(int value, int mod) {
        return ((value % mod) + mod) % mod;
    }

    // Time: O(log power), Space: O(1)
    static long modPow(long base, long power, long mod) {
        long answer = 1;
        base %= mod;

        while (power > 0) {
            if ((power & 1) == 1) {
                answer = (answer * base) % mod;
            }

            base = (base * base) % mod;
            power >>= 1;
        }

        return answer;
    }

    // Time: O(log n), Space: O(1)
    static double myPow(double x, int n) {
        long power = n;

        if (power < 0) {
            x = 1 / x;
            power = -power;
        }

        double answer = 1.0;

        while (power > 0) {
            if ((power & 1) == 1) {
                answer *= x;
            }

            x *= x;
            power >>= 1;
        }

        return answer;
    }

    static long factorialMod(int n, long mod) {
        long answer = 1;

        for (int i = 2; i <= n; i++) {
            answer = (answer * i) % mod;
        }

        return answer;
    }

    static long nPr(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }

        long answer = 1;

        for (int i = 0; i < r; i++) {
            answer *= (n - i);
        }

        return answer;
    }

    static long nCr(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }

        r = Math.min(r, n - r);
        long answer = 1;

        for (int i = 1; i <= r; i++) {
            answer = answer * (n - r + i) / i;
        }

        return answer;
    }

    static long modInverse(long value, long mod) {
        return modPow(value, mod - 2, mod);
    }

    // Time: O(log n), Space: O(1)
    static int trailingZeroes(int n) {
        int count = 0;

        while (n > 0) {
            n /= 5;
            count += n;
        }

        return count;
    }

    static boolean isHappy(int n) {
        Set<Integer> seen = new HashSet<>();

        while (n != 1 && !seen.contains(n)) {
            seen.add(n);
            n = nextHappyValue(n);
        }

        return n == 1;
    }

    static int nextHappyValue(int n) {
        int sum = 0;

        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }

        return sum;
    }

    static int titleToNumber(String columnTitle) {
        int answer = 0;

        for (int i = 0; i < columnTitle.length(); i++) {
            int value = columnTitle.charAt(i) - 'A' + 1;
            answer = answer * 26 + value;
        }

        return answer;
    }

    static class CombinationMod {
        long mod;
        long[] fact;
        long[] invFact;

        CombinationMod(int n, long mod) {
            this.mod = mod;
            this.fact = new long[n + 1];
            this.invFact = new long[n + 1];

            fact[0] = 1;
            for (int i = 1; i <= n; i++) {
                fact[i] = (fact[i - 1] * i) % mod;
            }

            invFact[n] = modPow(fact[n], mod - 2, mod);
            for (int i = n - 1; i >= 0; i--) {
                invFact[i] = (invFact[i + 1] * (i + 1)) % mod;
            }
        }

        long nCr(int n, int r) {
            if (r < 0 || r > n) {
                return 0;
            }

            return (((fact[n] * invFact[r]) % mod) * invFact[n - r]) % mod;
        }
    }
}

