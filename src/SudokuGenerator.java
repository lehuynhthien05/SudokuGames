import javax.swing.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SudokuGenerator {
    private static final int GRID_SIZE = 9;

    private boolean isValid(char[][] board, int row, int col, char num) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }


    public void generateRandomNumbers(char[][] board, Algorithm algorithm) {
        Random random = new Random();
        int count = 0;

        while (count < 10) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            if (board[row][col] == '.') {
                char num = (char) ('1' + random.nextInt(9));
                if (algorithm.isValid(board, row, col, num)) {
                    board[row][col] = num;
                    count++;
                }
            }
        }
    }
}
