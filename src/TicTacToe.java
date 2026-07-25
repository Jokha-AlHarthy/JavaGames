import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXTurn = true;
    private JLabel statusLabel;
    private int moveCount = 0;

    public TicTacToe() {
        setTitle("Tic-Tac-Toe Game");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status Label at top
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
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

        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        // If button is already clicked, ignore
        if (!clickedButton.getText().equals("")) {
            return;
        }

        // Set player mark
        if (isXTurn) {
            clickedButton.setText("X");
            clickedButton.setForeground(Color.BLUE);
            statusLabel.setText("Player O's Turn");
        } else {
            clickedButton.setText("O");
            clickedButton.setForeground(Color.RED);
            statusLabel.setText("Player X's Turn");
        }

        moveCount++;

        // Check for Win or Draw
        if (checkWin()) {
            String winner = isXTurn ? "X" : "O";
            statusLabel.setText("Player " + winner + " Wins!");
            disableBoard();
        } else if (moveCount == 9) {
            statusLabel.setText("It's a Draw!");
        } else {
            isXTurn = !isXTurn; // Switch turn
        }
    }

    private boolean checkWin() {
        String currentMark = isXTurn ? "X" : "O";

        // Check Rows & Columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(currentMark) &&
                    buttons[i][1].getText().equals(currentMark) &&
                    buttons[i][2].getText().equals(currentMark)) return true;

            if (buttons[0][i].getText().equals(currentMark) &&
                    buttons[1][i].getText().equals(currentMark) &&
                    buttons[2][i].getText().equals(currentMark)) return true;
        }

        // Check Diagonals
        if (buttons[0][0].getText().equals(currentMark) &&
                buttons[1][1].getText().equals(currentMark) &&
                buttons[2][2].getText().equals(currentMark)) return true;

        if (buttons[0][2].getText().equals(currentMark) &&
                buttons[1][1].getText().equals(currentMark) &&
                buttons[2][0].getText().equals(currentMark)) return true;

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
        statusLabel.setText("Player X's Turn");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}