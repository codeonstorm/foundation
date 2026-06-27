# 07. Board And Grid Backtracking

Many backtracking problems happen on a board or grid.

Common state:

- Current row and column
- Board contents
- Visited cells
- Current path

## N-Queens

Problem:

```text
Place n queens on an n x n chessboard so no two queens attack each other.
```

A queen attacks:

- Same row
- Same column
- Same diagonal

## N-Queens Strategy

Place one queen per row.

At each row, try every column.

If a position is safe:

- Place queen
- Recurse to next row
- Remove queen

Java:

```java
List<List<String>> solveNQueens(int n) {
    List<List<String>> answer = new ArrayList<>();
    char[][] board = new char[n][n];

    for (int r = 0; r < n; r++) {
        Arrays.fill(board[r], '.');
    }

    backtrackQueens(0, board, answer);
    return answer;
}

void backtrackQueens(int row, char[][] board, List<List<String>> answer) {
    int n = board.length;

    if (row == n) {
        answer.add(buildBoard(board));
        return;
    }

    for (int col = 0; col < n; col++) {
        if (!isSafeQueen(board, row, col)) {
            continue;
        }

        board[row][col] = 'Q';
        backtrackQueens(row + 1, board, answer);
        board[row][col] = '.';
    }
}
```

Safety check:

```java
boolean isSafeQueen(char[][] board, int row, int col) {
    for (int r = 0; r < row; r++) {
        if (board[r][col] == 'Q') {
            return false;
        }
    }

    for (int r = row - 1, c = col - 1; r >= 0 && c >= 0; r--, c--) {
        if (board[r][c] == 'Q') {
            return false;
        }
    }

    for (int r = row - 1, c = col + 1; r >= 0 && c < board.length; r--, c++) {
        if (board[r][c] == 'Q') {
            return false;
        }
    }

    return true;
}
```

Time complexity is often described as `O(n!)` search space, with safety-check overhead depending on implementation.

## Sudoku Solver

Problem:

```text
Fill a 9 x 9 Sudoku board so every row, column, and 3 x 3 box contains digits 1 to 9.
```

Strategy:

- Find an empty cell.
- Try digits `1` to `9`.
- If valid, place digit.
- Recurse.
- If recursion fails, erase digit.

Java:

```java
boolean solveSudoku(char[][] board) {
    for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
            if (board[row][col] == '.') {
                for (char digit = '1'; digit <= '9'; digit++) {
                    if (isValidSudoku(board, row, col, digit)) {
                        board[row][col] = digit;

                        if (solveSudoku(board)) {
                            return true;
                        }

                        board[row][col] = '.';
                    }
                }

                return false;
            }
        }
    }

    return true;
}
```

The `return false` after trying all digits means:

```text
This empty cell cannot be filled with any valid digit under the current choices.
```

## Rat In A Maze

Problem:

```text
Find paths from top-left to bottom-right in a grid.
1 means open cell.
0 means blocked cell.
```

Directions often used:

```text
D, L, R, U
```

Java:

```java
void maze(int[][] grid, int row, int col, boolean[][] visited, StringBuilder path, List<String> answer) {
    int n = grid.length;

    if (row == n - 1 && col == n - 1) {
        answer.add(path.toString());
        return;
    }

    visited[row][col] = true;

    int[] dr = {1, 0, 0, -1};
    int[] dc = {0, -1, 1, 0};
    char[] move = {'D', 'L', 'R', 'U'};

    for (int i = 0; i < 4; i++) {
        int nextRow = row + dr[i];
        int nextCol = col + dc[i];

        if (isOpen(grid, visited, nextRow, nextCol)) {
            path.append(move[i]);
            maze(grid, nextRow, nextCol, visited, path, answer);
            path.deleteCharAt(path.length() - 1);
        }
    }

    visited[row][col] = false;
}
```

## Word Search

Problem:

```text
Given a board and a word, return true if the word exists in the grid.
Adjacent cells are horizontal or vertical.
The same cell cannot be reused in one path.
```

Java:

```java
boolean exist(char[][] board, String word) {
    for (int row = 0; row < board.length; row++) {
        for (int col = 0; col < board[0].length; col++) {
            if (searchWord(board, word, row, col, 0)) {
                return true;
            }
        }
    }

    return false;
}

boolean searchWord(char[][] board, String word, int row, int col, int index) {
    if (index == word.length()) {
        return true;
    }

    if (row < 0 || col < 0 || row == board.length || col == board[0].length) {
        return false;
    }

    if (board[row][col] != word.charAt(index)) {
        return false;
    }

    char original = board[row][col];
    board[row][col] = '#';

    boolean found = searchWord(board, word, row + 1, col, index + 1)
            || searchWord(board, word, row - 1, col, index + 1)
            || searchWord(board, word, row, col + 1, index + 1)
            || searchWord(board, word, row, col - 1, index + 1);

    board[row][col] = original;
    return found;
}
```

This uses the board itself as a visited marker.

## Grid Backtracking Checklist

- Check boundaries first.
- Check blocked or invalid cells.
- Mark visited before recursive calls.
- Unmark visited after recursive calls.
- Be careful with shared board state.
- Return immediately if only one valid answer is needed.

