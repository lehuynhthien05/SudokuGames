import javax.swing.*;
import javax.swing.border.AbstractBorder;

import java.awt.geom.RoundRectangle2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



public class SudokuUI {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int CELL_SIZE = 50;
    private static final Color LIGHT_BG = new Color(250, 243, 224); // Cream Background
    private static final Color GRID_COLOR = new Color(125, 90, 80); // Muted Brown
    private static final Color HIGHLIGHT_COLOR = new Color(212, 239, 223); // Light Green
    private static final Color BACKGROUND_COLOR = new Color(250, 243, 224); // Cream
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // Dark Blue
    private static final Color INCORRECT_COLOR = new Color(255, 111, 97); // Coral
    private static final Color BUTTON_COLOR = new Color(125, 90, 80); // Muted Brown

    private boolean isDraftMode = false;
    private String[][] draftValues = new String[GRID_SIZE][GRID_SIZE];
    private boolean[][] validatedCells = new boolean[GRID_SIZE][GRID_SIZE];


    private JFrame frame;
    private JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];
    private JPanel boardPanel, buttonPanel;
    private JLabel statusLabel;

    Algorithm algorithm = new Algorithm();
    Hint hint = new Hint(algorithm);
    Undo undo = new Undo();
    char[][] board = new char[GRID_SIZE][GRID_SIZE];


    class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(125, 90, 80));
        g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        g2d.dispose();
    }
}

    public SudokuUI() {
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GRID_SIZE * CELL_SIZE + 150, GRID_SIZE * CELL_SIZE);
        frame.setLayout(new BorderLayout());

        setupDifficultySelection();
    }

    private void setupDifficultySelection() {
        JPanel difficultyPanel = new JPanel(new GridBagLayout());
        difficultyPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;  
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 20, 10, 20);

        JButton easyButton = createStyledButton("Easy");
        JButton mediumButton = createStyledButton("Medium");
        JButton hardButton = createStyledButton("Hard");

        // Initial button size (adjusted dynamically later)
        Dimension buttonSize = new Dimension(200, 50);
        easyButton.setPreferredSize(buttonSize);
        mediumButton.setPreferredSize(buttonSize);
        hardButton.setPreferredSize(buttonSize);

        easyButton.addActionListener(e -> startGame());
        mediumButton.addActionListener(e -> startGame());
        hardButton.addActionListener(e -> startGame());

        gbc.gridy = 0;
        difficultyPanel.add(easyButton, gbc);

        gbc.gridy = 1;
        difficultyPanel.add(mediumButton, gbc);

        gbc.gridy = 2;
        difficultyPanel.add(hardButton, gbc);

        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(difficultyPanel, new GridBagConstraints());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Add a listener to resize buttons dynamically
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = frame.getWidth();
                int height = frame.getHeight();

                // Set button size dynamically based on screen size
                int buttonWidth = Math.max(width / 5, 200);  // At least 200px wide
                int buttonHeight = Math.max(height / 15, 50); // At least 50px tall
                
                Dimension newSize = new Dimension(buttonWidth, buttonHeight);
                easyButton.setPreferredSize(newSize);
                mediumButton.setPreferredSize(newSize);
                hardButton.setPreferredSize(newSize);

                difficultyPanel.revalidate();
                difficultyPanel.repaint();
            }
        });

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isContentAreaFilled()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(200, 150, 100));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    
                    g2.dispose();
                }
                super.paintComponent(g);
            }
    
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
                // Border color
                g2.setColor(new Color(150, 100, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
    
                g2.dispose();
            }
        };
    
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 50));
    
        return button;
    }

    private JTextField selectedCell = null; // Track the selected cell

    private void startGame() {
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.repaint();
    
        boardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        boardPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel = new JPanel(new GridLayout(3, 3, 5, 5)); // Adjusted with spacing
        buttonPanel.setBackground(BACKGROUND_COLOR);
    
        JButton hintButton = createStyledButton("Hint");
        JButton undoButton = createStyledButton("Undo");
        JButton eraseButton = createStyledButton("Erase");
        JButton draftButton = createStyledButton("Draft");
    
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(TEXT_COLOR);
    
        hintButton.addActionListener(e -> {
            if (hint.provideHint(board, cells, undo)) {
                statusLabel.setText("Hint provided.");
            } else {
                statusLabel.setText("No hints available.");
            }
        });
    
        undoButton.addActionListener(e -> {
            if (undo.undo(board, cells)) {
                statusLabel.setText("Undo successful.");
            } else {
                statusLabel.setText("Nothing to undo.");
            }
        });
    
        draftButton.addActionListener(e -> toggleDraftMode());
        eraseButton.addActionListener(e -> {
            if (selectedCell != null) {
                selectedCell.setText("");
            }
        });
    
        for (int i = 1; i <= 9; i++) {
            JButton digitButton = createStyledButton(String.valueOf(i));
            int digit = i;
        
            digitButton.setPreferredSize(new Dimension(50, 50)); // Adjust button size
            digitButton.setFocusPainted(false);
            digitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
            // Rounded button effect
            digitButton.setBorder(new RoundedBorder(15));
        
            digitButton.addActionListener(e -> {
                if (selectedCell != null) {
                    selectedCell.setText(String.valueOf(digit));
                    selectedCell.setBackground(LIGHT_BG);
                }
            });
        
            buttonPanel.add(digitButton);
        }
    
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                cell.setForeground(TEXT_COLOR);
                cell.setBackground(LIGHT_BG);
                cell.setCaretColor(new Color(0, 0, 0, 0));
                cell.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    
                boolean isTop = (row % SUBGRID_SIZE == 0);
                boolean isLeft = (col % SUBGRID_SIZE == 0);
                boolean isBottom = ((row + 1) % SUBGRID_SIZE == 0);
                boolean isRight = ((col + 1) % SUBGRID_SIZE == 0);
                int borderSize = 3;
                int thinBorder = 1;
    
                cell.setBorder(BorderFactory.createMatteBorder(
                        isTop ? borderSize : thinBorder,
                        isLeft ? borderSize : thinBorder,
                        isBottom ? borderSize : thinBorder,
                        isRight ? borderSize : thinBorder,
                        GRID_COLOR));
    
                cells[row][col] = cell;
                boardPanel.add(cell);
    
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedCell != null) {
                            selectedCell.setBackground(LIGHT_BG);
                        }
                        selectedCell = (JTextField) e.getSource();
                        selectedCell.setBackground(Color.GRAY);
                    }
                });
            }
        }
    
        JPanel sidePanel = new JPanel(new BorderLayout());
        JPanel buttonContainer = new JPanel(new GridLayout(4, 1));
        buttonContainer.add(hintButton);
        buttonContainer.add(undoButton);
        buttonContainer.add(eraseButton);
        buttonContainer.add(draftButton);
    
        sidePanel.add(buttonPanel, BorderLayout.CENTER);
        sidePanel.add(buttonContainer, BorderLayout.SOUTH);
    
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
        frame.add(statusLabel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();

        // Generate random numbers for easy level

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board[row][col] = '.';
            }
        }
        SudokuGenerator generator = new SudokuGenerator();
        generator.generateRandomNumbers(board, algorithm);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] != '.') {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                }
            }
        }
    }

    public void toggleDraftMode() {
        isDraftMode = !isDraftMode;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JTextField cell = cells[row][col];
                if (isDraftMode) {
                    if (!validatedCells[row][col]) {
                        cell.setFont(new Font("Arial", Font.ITALIC, 16));
                        cell.setForeground(Color.GRAY);
                    }
                } else {
                    if (cell.getText().isEmpty() && draftValues[row][col] != null) {
                        cell.setText(draftValues[row][col]);
                        cell.setFont(new Font("Arial", Font.ITALIC, 16));
                        cell.setForeground(Color.GRAY);
                    } else {
                        cell.setFont(new Font("Arial", Font.BOLD, 22));
                        cell.setForeground(TEXT_COLOR);
                    }
                }
            }
        }
        if (!isDraftMode) {
            validateSudoku(); // Auto validate when exiting draft mode
        }
    }

    private void validateSudoku() {
        if (isDraftMode) {
            toggleDraftMode();  // Exit draft mode
            return;  // Avoid validate twice
        }

        char[][] board = new char[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String text = cells[row][col].getText().trim();
                board[row][col] = text.isEmpty() ? '.' : text.charAt(0);
            }
        }

        Algorithm algorithm = new Algorithm();
        boolean isValid = algorithm.isValidSudoku(board);

        JOptionPane.showMessageDialog(
            frame,
            isValid ? "Congratulations! The Sudoku is valid!" : "Oops! The Sudoku is invalid.",
            "Validation Result",
            isValid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );

        if (isValid) {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (board[row][col] != '.') {
                        validatedCells[row][col] = true;
                    }
                }
            }
        }

    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuUI::new);
    }
}
