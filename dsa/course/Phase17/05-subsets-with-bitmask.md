# 05. Subsets With Bitmask

A subset can be represented by a bit mask.

For an array:

```text
nums = [10, 20, 30]
```

A mask with 3 bits can represent which elements are selected.

```text
mask 000 -> []
mask 001 -> [10]
mask 010 -> [20]
mask 011 -> [10, 20]
mask 100 -> [30]
mask 101 -> [10, 30]
mask 110 -> [20, 30]
mask 111 -> [10, 20, 30]
```

## Number Of Subsets

Each element has two choices:

```text
selected
not selected
```

For `n` elements:

```text
total subsets = 2^n = 1 << n
```

## Generate Subsets

Java:

```java
List<List<Integer>> subsets(int[] nums) {
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
```

Time:

```text
O(n * 2^n)
```

Space:

```text
O(n * 2^n) for storing all subsets
```

Auxiliary space excluding answer:

```text
O(n)
```

## Print Masks In Binary

```java
String binary = Integer.toBinaryString(mask);
```

To pad with leading zeroes:

```java
String padded = String.format("%" + n + "s", binary).replace(' ', '0');
```

This is useful for learning and debugging.

## Subset Sum With Bitmask

Problem:

```text
Print the sum of every subset.
```

Java:

```java
List<Integer> subsetSums(int[] nums) {
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
```

## Iterate Over Selected Bits Only

Instead of checking every bit from `0` to `n - 1`, you can iterate over selected bits.

```java
void printSelectedBits(int mask) {
    while (mask != 0) {
        int lastBit = mask & -mask;
        int bitIndex = Integer.numberOfTrailingZeros(lastBit);
        System.out.println(bitIndex);
        mask = mask & (mask - 1);
    }
}
```

This is useful when masks are sparse.

## Iterate Over All Submasks

For advanced problems, you may need every submask of a mask.

```java
for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
    System.out.println(submask);
}
```

To include `0`:

```java
for (int submask = mask; ; submask = (submask - 1) & mask) {
    System.out.println(submask);

    if (submask == 0) {
        break;
    }
}
```

This pattern appears in advanced bitmask DP.

## When To Use Bitmask Subsets

Use this when:

- You need all subsets.
- `n` is small, usually `n <= 20`.
- You need compact subset representation.
- You want iterative subset generation instead of recursion.

## Recursion Vs Bitmask For Subsets

| Approach | Good for |
| --- | --- |
| Recursion/backtracking | Easier to add constraints and pruning |
| Bitmask | Compact, iterative, useful for subset DP |

