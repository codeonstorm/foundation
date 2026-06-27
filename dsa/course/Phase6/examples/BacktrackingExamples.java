import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BacktrackingExamples {
    public static void main(String[] args) {
        System.out.println("Subsets: " + subsets(new int[]{1, 2, 3}));
        System.out.println("Subsequences of 'ab': " + subsequences("ab"));
        System.out.println("Permutations: " + permute(new int[]{1, 2, 3}));
        System.out.println("Combination sum: " + combinationSum(new int[]{2, 3, 6, 7}, 7));
        System.out.println("Parentheses n=3: " + generateParenthesis(3));
        System.out.println("Letter combinations 23: " + letterCombinations("23"));
        System.out.println("Palindrome partitions of aab: " + partition("aab"));
        System.out.println("Restore IP: " + restoreIpAddresses("25525511135"));
        System.out.println("N-Queens solutions for n=4: " + solveNQueens(4).size());

        int[][] maze = {
                {1, 0, 0, 0},
                {1, 1, 0, 1},
                {1, 1, 0, 0},
                {0, 1, 1, 1}
        };
        System.out.println("Maze paths: " + findPaths(maze));

        char[][] wordBoard = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        System.out.println("Word search ABCCED: " + exist(wordBoard, "ABCCED"));

        char[][] sudoku = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        solveSudoku(sudoku);
        System.out.println("Sudoku first row after solve: " + Arrays.toString(sudoku[0]));
    }

    // Time: O(n * 2^n), Space: O(n) excluding answer storage
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> answer = new ArrayList<>();
        subsets(nums, 0, new ArrayList<>(), answer);
        return answer;
    }

    static void subsets(int[] nums, int index, List<Integer> current, List<List<Integer>> answer) {
        if (index == nums.length) {
            answer.add(new ArrayList<>(current));
            return;
        }

        subsets(nums, index + 1, current, answer);

        current.add(nums[index]);
        subsets(nums, index + 1, current, answer);
        current.remove(current.size() - 1);
    }

    // Time: O(n * 2^n), Space: O(n) excluding answer storage
    static List<String> subsequences(String text) {
        List<String> answer = new ArrayList<>();
        subsequences(text, 0, new StringBuilder(), answer);
        return answer;
    }

    static void subsequences(String text, int index, StringBuilder current, List<String> answer) {
        if (index == text.length()) {
            answer.add(current.toString());
            return;
        }

        subsequences(text, index + 1, current, answer);

        current.append(text.charAt(index));
        subsequences(text, index + 1, current, answer);
        current.deleteCharAt(current.length() - 1);
    }

    // Time: O(n * n!), Space: O(n) excluding answer storage
    static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> answer = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        permute(nums, used, new ArrayList<>(), answer);
        return answer;
    }

    static void permute(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> answer) {
        if (current.size() == nums.length) {
            answer.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }

            used[i] = true;
            current.add(nums[i]);

            permute(nums, used, current, answer);

            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> answer = new ArrayList<>();
        Arrays.sort(candidates);
        combinationSum(candidates, target, 0, new ArrayList<>(), answer);
        return answer;
    }

    static void combinationSum(int[] candidates, int remaining, int start, List<Integer> current, List<List<Integer>> answer) {
        if (remaining == 0) {
            answer.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) {
                break;
            }

            current.add(candidates[i]);
            combinationSum(candidates, remaining - candidates[i], i, current, answer);
            current.remove(current.size() - 1);
        }
    }

    static List<String> generateParenthesis(int n) {
        List<String> answer = new ArrayList<>();
        generateParenthesis(n, 0, 0, new StringBuilder(), answer);
        return answer;
    }

    static void generateParenthesis(int n, int open, int close, StringBuilder current, List<String> answer) {
        if (current.length() == 2 * n) {
            answer.add(current.toString());
            return;
        }

        if (open < n) {
            current.append('(');
            generateParenthesis(n, open + 1, close, current, answer);
            current.deleteCharAt(current.length() - 1);
        }

        if (close < open) {
            current.append(')');
            generateParenthesis(n, open, close + 1, current, answer);
            current.deleteCharAt(current.length() - 1);
        }
    }

    static List<String> letterCombinations(String digits) {
        List<String> answer = new ArrayList<>();

        if (digits.length() == 0) {
            return answer;
        }

        String[] map = {
                "", "", "abc", "def", "ghi",
                "jkl", "mno", "pqrs", "tuv", "wxyz"
        };

        letterCombinations(digits, 0, map, new StringBuilder(), answer);
        return answer;
    }

    static void letterCombinations(String digits, int index, String[] map, StringBuilder current, List<String> answer) {
        if (index == digits.length()) {
            answer.add(current.toString());
            return;
        }

        String letters = map[digits.charAt(index) - '0'];

        for (int i = 0; i < letters.length(); i++) {
            current.append(letters.charAt(i));
            letterCombinations(digits, index + 1, map, current, answer);
            current.deleteCharAt(current.length() - 1);
        }
    }

    static List<List<String>> partition(String text) {
        List<List<String>> answer = new ArrayList<>();
        partition(text, 0, new ArrayList<>(), answer);
        return answer;
    }

    static void partition(String text, int start, List<String> current, List<List<String>> answer) {
        if (start == text.length()) {
            answer.add(new ArrayList<>(current));
            return;
        }

        for (int end = start; end < text.length(); end++) {
            if (isPalindrome(text, start, end)) {
                current.add(text.substring(start, end + 1));
                partition(text, end + 1, current, answer);
                current.remove(current.size() - 1);
            }
        }
    }

    static boolean isPalindrome(String text, int left, int right) {
        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }

    static List<String> restoreIpAddresses(String text) {
        List<String> answer = new ArrayList<>();
        restoreIpAddresses(text, 0, new ArrayList<>(), answer);
        return answer;
    }

    static void restoreIpAddresses(String text, int index, List<String> parts, List<String> answer) {
        if (parts.size() == 4) {
            if (index == text.length()) {
                answer.add(String.join(".", parts));
            }
            return;
        }

        for (int len = 1; len <= 3; len++) {
            if (index + len > text.length()) {
                break;
            }

            String part = text.substring(index, index + len);

            if (!isValidIpPart(part)) {
                continue;
            }

            parts.add(part);
            restoreIpAddresses(text, index + len, parts, answer);
            parts.remove(parts.size() - 1);
        }
    }

    static boolean isValidIpPart(String part) {
        if (part.length() > 1 && part.charAt(0) == '0') {
            return false;
        }

        return Integer.parseInt(part) <= 255;
    }

    static List<List<String>> solveNQueens(int n) {
        List<List<String>> answer = new ArrayList<>();
        char[][] board = new char[n][n];

        for (int row = 0; row < n; row++) {
            Arrays.fill(board[row], '.');
        }

        solveNQueens(0, board, answer);
        return answer;
    }

    static void solveNQueens(int row, char[][] board, List<List<String>> answer) {
        if (row == board.length) {
            answer.add(buildBoard(board));
            return;
        }

        for (int col = 0; col < board.length; col++) {
            if (!isSafeQueen(board, row, col)) {
                continue;
            }

            board[row][col] = 'Q';
            solveNQueens(row + 1, board, answer);
            board[row][col] = '.';
        }
    }

    static boolean isSafeQueen(char[][] board, int row, int col) {
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

    static List<String> buildBoard(char[][] board) {
        List<String> result = new ArrayList<>();

        for (char[] row : board) {
            result.add(new String(row));
        }

        return result;
    }

    static boolean solveSudoku(char[][] board) {
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

    static boolean isValidSudoku(char[][] board, int row, int col, char digit) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == digit) {
                return false;
            }

            if (board[i][col] == digit) {
                return false;
            }

            int boxRow = 3 * (row / 3) + i / 3;
            int boxCol = 3 * (col / 3) + i % 3;

            if (board[boxRow][boxCol] == digit) {
                return false;
            }
        }

        return true;
    }

    static List<String> findPaths(int[][] grid) {
        List<String> answer = new ArrayList<>();

        if (grid.length == 0 || grid[0][0] == 0) {
            return answer;
        }

        boolean[][] visited = new boolean[grid.length][grid.length];
        findPaths(grid, 0, 0, visited, new StringBuilder(), answer);
        return answer;
    }

    static void findPaths(int[][] grid, int row, int col, boolean[][] visited, StringBuilder path, List<String> answer) {
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
                findPaths(grid, nextRow, nextCol, visited, path, answer);
                path.deleteCharAt(path.length() - 1);
            }
        }

        visited[row][col] = false;
    }

    static boolean isOpen(int[][] grid, boolean[][] visited, int row, int col) {
        int n = grid.length;

        return row >= 0
                && col >= 0
                && row < n
                && col < n
                && grid[row][col] == 1
                && !visited[row][col];
    }

    static boolean exist(char[][] board, String word) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (searchWord(board, word, row, col, 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean searchWord(char[][] board, String word, int row, int col, int index) {
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
}

