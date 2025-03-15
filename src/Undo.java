import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Undo {
    private Stack<Action> actions;

    public Undo() {
        actions = new Stack<>();
    }

    public void addAction(int row, int col, String value, Color color) {
        actions.push(new Action(row, col, value, color));
    }

    public boolean undo(char[][] board, JTextField[][] cells) {
        if (actions.isEmpty()) {
            return false;
        }

        Action action = actions.pop();
        int row = action.getRow();
        int col = action.getCol();
        String prevValue = action.getValue();
        Color prevColor = action.getColor();

        cells[row][col].setText(prevValue);
        cells[row][col].setBackground(prevColor);

        board[row][col] = prevValue.isEmpty() ? '.' : prevValue.charAt(0);

        return true;
    }

    public void clear() {
        actions.clear();
    }

    private static class Action {
        private final int row;
        private final int col;
        private final String value;
        private final Color color;

        public Action(int row, int col, String value, Color color) {
            this.row = row;
            this.col = col;
            this.value = value;
            this.color = color;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public String getValue() {
            return value;
        }

        public Color getColor() {
            return color;
        }
    }
}