import java.util.Random;

public class SudokuGenerator {
    private static final int GRID_SIZE = 9;
    private final Random random = new Random();

    public void generateSudoku(char[][] board) {
        fillBoard(board);
        removeNumbers(board);
    }

    private boolean fillBoard(char[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == '.') {
                    for (char num = '1'; num <= '9'; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard(board)) {
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

    private void removeNumbers(char[][] board) {
        int cellsToRemove = 40; // Number of cells to remove for the puzzle
        while (cellsToRemove > 0) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            if (board[row][col] != '.') {
                board[row][col] = '.';
                cellsToRemove--;
            }
        }
    }

    private boolean isValid(char[][] board, int row, int col, char num) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }
}