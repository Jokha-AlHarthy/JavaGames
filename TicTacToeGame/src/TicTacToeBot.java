import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeBot extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXTurn = true; // Human is X, Smart Bot is O
    private JLabel statusLabel;
    private int moveCount = 0;

    public TicTacToeBot() {
        setTitle("Tic-Tac-Toe (Vs Smart AI)");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status Label at top
        statusLabel = new JLabel("Your Turn (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(statusLabel, BorderLayout.NORTH);

        // 3x3 Grid Panel for buttons
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(this);
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Restart Button at bottom
        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.addActionListener(e -> resetGame());
        add(restartButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        // Ignore click if cell taken or not human turn
        if (!clickedButton.getText().equals("") || !isXTurn) {
            return;
        }

        // --- Human Move (Player X) ---
        makeMove(clickedButton, "X", Color.BLUE);

        if (checkGameEnd("X")) {
            return;
        }

        // Switch turn to Bot
        isXTurn = false;
        statusLabel.setText("Bot is thinking...");

        // --- Bot Move (Player O) with slight delay ---
        Timer timer = new Timer(300, event -> {
            makeBestBotMove();
            if (!checkGameEnd("O")) {
                isXTurn = true;
                statusLabel.setText("Your Turn (X)");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void makeMove(JButton button, String symbol, Color color) {
        button.setText(symbol);
        button.setForeground(color);
        moveCount++;
    }

    // --- Minimax AI Logic ---
    private void makeBestBotMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText("O"); // Simulate move
                    int score = minimax(false, 0);
                    buttons[i][j].setText(""); // Undo move

                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        if (bestRow != -1 && bestCol != -1) {
            makeMove(buttons[bestRow][bestCol], "O", Color.RED);
        }
    }

    private int minimax(boolean isMaximizing, int depth) {
        if (checkWin("O")) return 10 - depth; // AI wins (favour quicker wins)
        if (checkWin("X")) return depth - 10; // Human wins (favour longer delays if losing)
        if (isBoardFull()) return 0;         // Draw

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals("")) {
                        buttons[i][j].setText("O");
                        int score = minimax(false, depth + 1);
                        buttons[i][j].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals("")) {
                        buttons[i][j].setText("X");
                        int score = minimax(true, depth + 1);
                        buttons[i][j].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) return false;
            }
        }
        return true;
    }

    private boolean checkGameEnd(String symbol) {
        if (checkWin(symbol)) {
            String winnerMessage = symbol.equals("X") ? "You Win!" : "Bot Wins!";
            statusLabel.setText(winnerMessage);
            disableBoard();
            return true;
        } else if (isBoardFull()) {
            statusLabel.setText("It's a Draw!");
            return true;
        }
        return false;
    }

    private boolean checkWin(String mark) {
        // Rows & Columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(mark) &&
                    buttons[i][1].getText().equals(mark) &&
                    buttons[i][2].getText().equals(mark)) return true;

            if (buttons[0][i].getText().equals(mark) &&
                    buttons[1][i].getText().equals(mark) &&
                    buttons[2][i].getText().equals(mark)) return true;
        }

        // Diagonals
        if (buttons[0][0].getText().equals(mark) &&
                buttons[1][1].getText().equals(mark) &&
                buttons[2][2].getText().equals(mark)) return true;

        if (buttons[0][2].getText().equals(mark) &&
                buttons[1][1].getText().equals(mark) &&
                buttons[2][0].getText().equals(mark)) return true;

        return false;
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        isXTurn = true;
        moveCount = 0;
        statusLabel.setText("Your Turn (X)");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeBot::new);
    }
}