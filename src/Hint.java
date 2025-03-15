import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Hint {
    private static final Color HINT_COLOR = new Color(212, 239, 223);
    private static final int MAX_HINTS = 3;
    private char[][] initialSolution;
    private int hintsRemaining = MAX_HINTS;
    private final Random random = new Random();
    private final Algorithm algorithm;

    public Hint(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setInitialSolution(char[][] solution) {
        this.initialSolution = new char[9][9];
        for (int i = 0; i < 9; i++) {
            this.initialSolution[i] = solution[i].clone();
        }
    }
    public boolean provideHint(char[][] board, JTextField[][] cells, Undo undo) {
        if (hintsRemaining <= 0) {
            return false;
        }

        if (initialSolution == null || initialSolution.length == 0) {
            return false;
        }

        ArrayList<int[]> availableHints = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != '.') {
                    availableHints.add(new int[] { row, col });
                }
            }
        }

        if (availableHints.isEmpty()) {
            return false;
        }

        int index = random.nextInt(availableHints.size());
        int[] hintCell = availableHints.get(index);
        int row = hintCell[0];
        int col = hintCell[1];

        String prevValue = cells[row][col].getText();
        Color prevColor = cells[row][col].getBackground();
        undo.addAction(row, col, prevValue, prevColor);

        cells[row][col].setText(String.valueOf(initialSolution[row][col]));
        cells[row][col].setBackground(HINT_COLOR);

        hintsRemaining--;
        return true;
    }

    public int getHintsRemaining() {
        return hintsRemaining;
    }

    public void resetHints() {
        hintsRemaining = MAX_HINTS;
    }
}