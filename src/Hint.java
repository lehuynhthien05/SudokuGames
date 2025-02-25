import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Hint {
    private final Algorithm algorithm;
    private static final Color HINT_COLOR = new Color(212, 239, 223);

    public Hint(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public boolean provideHint(char[][] board, JTextField[][] cells, Undo undo) {
        Random random = new Random();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == '.') {
                    for (char num = '1'; num <= '9'; num++) {
                        if (algorithm.isValid(board, row, col, num)) {
                            String prevValue = cells[row][col].getText();
                            Color prevColor = cells[row][col].getBackground();
                            undo.addAction(row, col, prevValue, prevColor);

                            board[row][col] = num;
                            cells[row][col].setText(String.valueOf(num));
                            cells[row][col].setBackground(HINT_COLOR);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}