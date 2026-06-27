# 05. Grid DP

Grid DP usually uses a 2D state.

Common state:

```text
dp[row][col] = answer for reaching or solving cell row, col
```

Most grid DP transitions depend on neighboring cells:

- Top
- Left
- Bottom
- Right
- Diagonal

## Unique Paths

Problem:

```text
You are in the top-left cell of an m x n grid.
You can move only right or down.
How many ways can you reach the bottom-right cell?
```

State:

```text
dp[row][col] = number of ways to reach cell row, col
```

Transition:

```text
dp[row][col] = dp[row - 1][col] + dp[row][col - 1]
```

Base cases:

```text
First row = 1
First column = 1
```

Java:

```java
int uniquePaths(int rows, int cols) {
    int[][] dp = new int[rows][cols];

    for (int row = 0; row < rows; row++) {
        dp[row][0] = 1;
    }

    for (int col = 0; col < cols; col++) {
        dp[0][col] = 1;
    }

    for (int row = 1; row < rows; row++) {
        for (int col = 1; col < cols; col++) {
            dp[row][col] = dp[row - 1][col] + dp[row][col - 1];
        }
    }

    return dp[rows - 1][cols - 1];
}
```

Time: `O(rows * cols)`

Space: `O(rows * cols)`

## Unique Paths Space Optimized

Each cell only needs:

- Value from top
- Value from left

Use one row:

```java
int uniquePathsOptimized(int rows, int cols) {
    int[] dp = new int[cols];
    Arrays.fill(dp, 1);

    for (int row = 1; row < rows; row++) {
        for (int col = 1; col < cols; col++) {
            dp[col] = dp[col] + dp[col - 1];
        }
    }

    return dp[cols - 1];
}
```

Space: `O(cols)`

## Minimum Path Sum

Problem:

```text
Given a grid of costs, move only right or down.
Find minimum path sum from top-left to bottom-right.
```

State:

```text
dp[row][col] = minimum cost to reach cell row, col
```

Transition:

```text
dp[row][col] = grid[row][col] + min(dp[row - 1][col], dp[row][col - 1])
```

Java:

```java
int minPathSum(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    int[][] dp = new int[rows][cols];

    dp[0][0] = grid[0][0];

    for (int row = 1; row < rows; row++) {
        dp[row][0] = grid[row][0] + dp[row - 1][0];
    }

    for (int col = 1; col < cols; col++) {
        dp[0][col] = grid[0][col] + dp[0][col - 1];
    }

    for (int row = 1; row < rows; row++) {
        for (int col = 1; col < cols; col++) {
            dp[row][col] = grid[row][col] + Math.min(dp[row - 1][col], dp[row][col - 1]);
        }
    }

    return dp[rows - 1][cols - 1];
}
```

Time: `O(rows * cols)`

Space: `O(rows * cols)`

## Minimum Path Sum Space Optimized

```java
int minPathSumOptimized(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    int[] dp = new int[cols];

    dp[0] = grid[0][0];

    for (int col = 1; col < cols; col++) {
        dp[col] = dp[col - 1] + grid[0][col];
    }

    for (int row = 1; row < rows; row++) {
        dp[0] += grid[row][0];

        for (int col = 1; col < cols; col++) {
            dp[col] = grid[row][col] + Math.min(dp[col], dp[col - 1]);
        }
    }

    return dp[cols - 1];
}
```

## Grid With Obstacles

If a cell is blocked:

```text
dp[row][col] = 0
```

Otherwise:

```text
dp[row][col] = ways from top + ways from left
```

This pattern appears in obstacle path problems.

## How To Recognize Grid DP

Use grid DP when:

- You move through a matrix.
- You count paths.
- You minimize or maximize path cost.
- Movement directions are restricted.
- Current cell depends on previous neighboring cells.

## Grid DP Checklist

- What does `dp[row][col]` mean?
- Which cells can lead into this cell?
- What are the boundary cells?
- Are there blocked cells?
- Should you count ways, minimize cost, or maximize value?
- Can the space be reduced to one row?

