public class Algorithm {
    public boolean isValidSudoku(char[][] board) {
        for (int i = 0; i < 9; i++) {
            if (!isValidRow(board, i) || !isValidColumn(board, i) || !isValidSubBox(board, i))
                return false;
        }
        return true;
    }

    private boolean isValidRow(char[][] board, int row) {
        boolean[] seen = new boolean[10];
        for (int col = 0; col < 9; col++) {
            char digit = board[row][col];
            if (digit != '.') {
                if (seen[digit - '0'])
                    return false;
                seen[digit - '0'] = true;
            }
        }
        return true;
    }

    private boolean isValidColumn(char[][] board, int col) {
        boolean[] seen = new boolean[10];
        for (int row = 0; row < 9; row++) {
            char digit = board[row][col];
            if (digit != '.') {
                if (seen[digit - '0'])
                    return false;
                seen[digit - '0'] = true;
            }
        }
        return true;
    }

    private boolean isValidSubBox(char[][] board, int box) {
        boolean[] seen = new boolean[10];
        int rowOffset = (box / 3) * 3;
        int colOffset = (box % 3) * 3;
        for (int i = rowOffset; i < rowOffset + 3; i++) {
            for (int j = colOffset; j < colOffset + 3; j++) {
                char digit = board[i][j];
                if (digit != '.') {
                    if (seen[digit - '0'])
                        return false;
                    seen[digit - '0'] = true;
                }
            }
        }
        return true;
    }

    public boolean isValid(char[][] board, int row, int col, char num) {
        // Check the row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        // Check the column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        // Check the 3x3 subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean[][] getInvalidCells(char[][] board) {
        boolean[][] invalidCells = new boolean[9][9];
    
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!isValidPlacement(board, row, col)) { // Implement this method
                    invalidCells[row][col] = true;
                }
            }
        }
        return invalidCells;
    }
    
    // Checks if placing board[row][col] is valid
    private boolean isValidPlacement(char[][] board, int row, int col) {
        char num = board[row][col];
        if (num == '.') return true; // Empty cell is always valid
    
        // Check row
        for (int i = 0; i < 9; i++) {
            if (i != col && board[row][i] == num) return false;
        }
    
        // Check column
        for (int i = 0; i < 9; i++) {
            if (i != row && board[i][col] == num) return false;
        }
    
        // Check 3x3 box
        int startRow = (row / 3) * 3, startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int r = startRow + i, c = startCol + j;
                if ((r != row || c != col) && board[r][c] == num) return false;
            }
        }
        return true;
    }
    
}