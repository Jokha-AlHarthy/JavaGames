import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Player vs. Player", "Player vs. Smart Bot"};

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Welcome to Tic-Tac-Toe!\nChoose your game mode:",
                    "Tic-Tac-Toe Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                // Launch PvP Mode
                new TicTacToePvP();
            } else if (choice == 1) {
                // Launch Smart Bot Mode
                new TicTacToeBot();
            }
        });
    }
}