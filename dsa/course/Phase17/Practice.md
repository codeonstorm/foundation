# Phase 17 Practice

Try these before reading `Solutions.md`.

For each problem, write:

```text
Idea:
Key bit formula:
Time:
Space:
```

## Part A: Basic Bit Operations

### 1. Check Bit

Write:

```java
boolean isSet(int num, int bit)
```

Return true if bit `bit` is set.

### 2. Set Bit

Write:

```java
int setBit(int num, int bit)
```

### 3. Clear Bit

Write:

```java
int clearBit(int num, int bit)
```

### 4. Toggle Bit

Write:

```java
int toggleBit(int num, int bit)
```

### 5. Update Bit

Write:

```java
int updateBit(int num, int bit, int value)
```

`value` will be `0` or `1`.

## Part B: Counting And Power

### 6. Count Set Bits

Write:

```java
int countSetBits(int num)
```

Use Brian Kernighan's algorithm.

### 7. Power Of Two

Write:

```java
boolean isPowerOfTwo(int num)
```

### 8. Counting Bits

Write:

```java
int[] countBits(int n)
```

Return set-bit count for every number from `0` to `n`.

### 9. Hamming Distance

Write:

```java
int hammingDistance(int x, int y)
```

Return the number of bit positions where `x` and `y` are different.

## Part C: XOR Patterns

### 10. Single Number

Every number appears twice except one.

Write:

```java
int singleNumber(int[] nums)
```

### 11. Missing Number

The array contains `n` distinct numbers from `0` to `n`, with one missing.

Write:

```java
int missingNumber(int[] nums)
```

### 12. Two Single Numbers

Every number appears twice except two numbers.

Write:

```java
int[] twoSingleNumbers(int[] nums)
```

### 13. Single Number Among Triples

Every number appears three times except one.

Write:

```java
int singleNumberAmongTriples(int[] nums)
```

## Part D: Masks And Subsets

### 14. Generate Subsets

Use bitmasking.

Write:

```java
List<List<Integer>> subsets(int[] nums)
```

### 15. Subset Sums

Use bitmasking.

Write:

```java
List<Integer> subsetSums(int[] nums)
```

### 16. Count Selected Items

Given a mask, count selected items.

Write:

```java
int countSelected(int mask)
```

Do it with:

- Manual bit logic
- Java built-in

## Part E: Advanced Bit Patterns

### 17. Maximum XOR

Write:

```java
int findMaximumXOR(int[] nums)
```

Use the greedy prefix-set method.

### 18. Minimum Assignment Cost

Given `cost[person][task]`, assign one task to each person with minimum total cost.

Write:

```java
int minAssignmentCost(int[][] cost)
```

Use bitmask DP.

### 19. Explain A Mask

For:

```text
mask = 13
```

Write its binary representation and list selected bit positions.

### 20. Debug This Formula

What is wrong here?

```java
boolean isPowerOfTwo(int num) {
    return (num & (num - 1)) == 0;
}
```

Fix it.

## Final Reflection

For five problems, write:

```text
Why bit manipulation helps:
Which operator is central:
What is the mask:
What is the invariant:
```

