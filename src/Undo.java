import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Undo {
    private static class Action {
        int row, col;
        String prevValue;
        Color prevColor;

        public Action(int row, int col, String prevValue, Color prevColor) {
            this.row = row;
            this.col = col;
            this.prevValue = prevValue;
            this.prevColor = prevColor;
        }
    }

    private final Stack<Action> history = new Stack<>();

    public void addAction(int row, int col, String prevValue, Color prevColor) {
        history.push(new Action(row, col, prevValue, prevColor));
    }

    public boolean undo(char[][] board, JTextField[][] cells) {
        if (!history.isEmpty()) {
            Action action = history.pop();
            board[action.row][action.col] = action.prevValue.equals("") ? '.' : action.prevValue.charAt(0);
            cells[action.row][action.col].setText(action.prevValue);
            cells[action.row][action.col].setBackground(action.prevColor);
            return true;
        }
        return false;
    }

    public void clearHistory() {
        history.clear();
    }
}