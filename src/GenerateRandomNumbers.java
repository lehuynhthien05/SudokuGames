import javax.swing.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateRandomNumbers {
    private static final int GRID_SIZE = 9;

    public void generateRandomNumbers(JTextField[][] cells) {
        Random random = new Random();
        Set<String> filledCells = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            int row, col;
            String cellKey;

            do {
                row = random.nextInt(GRID_SIZE);
                col = random.nextInt(GRID_SIZE);
                cellKey = row + "," + col;
            } while (filledCells.contains(cellKey));

            filledCells.add(cellKey);
            int num = random.nextInt(9) + 1;
            cells[row][col].setText(String.valueOf(num));
        }
    }
    public void removeNumbers(char[][] board, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int row, col;
            do {
                row = random.nextInt(GRID_SIZE);
                col = random.nextInt(GRID_SIZE);
            } while (board[row][col] == '.');
            board[row][col] = '.';
        }
    }
}