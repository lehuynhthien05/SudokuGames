import javax.swing.JTextField;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Draft {

    private static final int GRID_SIZE = SudokuUI.GRID_SIZE;
    private boolean isDraftMode = false;
    private ArrayList<String>[][] draftValues;

    public Draft() {
        draftValues = new ArrayList[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                draftValues[row][col] = new ArrayList<>();
            }
        }
    }

    public void toggleDraftMode() {
        isDraftMode = !isDraftMode;

        // Update the draft status label
        SudokuUI.draftStatusLabel.setText(isDraftMode ? "Draft Mode: ON" : "Draft Mode: OFF");
        SudokuUI.draftStatusLabel.setForeground(isDraftMode ? Color.BLUE : Color.RED);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JTextField cell = SudokuUI.cells[row][col];
                if (isDraftMode) {

                    if (!SudokuUI.validatedCells[row][col]) {
                        cell.setFont(new Font("Arial", Font.ITALIC, 12));
                        cell.setForeground(Color.BLUE);
                        cell.setText(formatDraftValues(row, col));

                    }
                } else {

                    if (!SudokuUI.validatedCells[row][col]) {
                        cell.setFont(new Font("Arial", Font.BOLD, 20));
                        cell.setForeground(SudokuUI.TEXT_COLOR);
                        cell.setText("");
                        clearAllDraftValues();
                    }
                }
            }
        }
    }

    public boolean isDraftMode() {
        return isDraftMode;
    }

    public ArrayList<String> getDraftValues(int row, int col) {
        return draftValues[row][col];
    }

    // Format multiple draft values in a cell
    public String formatDraftValues(int row, int col) {
        return String.join(" ", draftValues[row][col]);
    }

    // Add a draft value to a cell
    public void addDraftValue(int row, int col, String value) {
        if (!draftValues[row][col].contains(value)) {
            draftValues[row][col].add(value);
        }
    }

    // Remove a draft value from a cell
    public void removeDraftValue(int row, int col, String value) {
        draftValues[row][col].remove(value);
    }


    // Clear all draft values for a cell
    public void clearDraftValues(int row, int col) {
        draftValues[row][col].clear();
    }

    // Clear all draft values
    public void clearAllDraftValues() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                draftValues[row][col].clear();
            }
        }
    }
}