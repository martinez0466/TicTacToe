package com.example.tictactoe6;

import com.example.tictactoe6.Model;
import javafx.animation.ScaleTransition;
import javafx.animation.PauseTransition;  // Importera PauseTransition för fördröjning
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent; // Import ActionEvent
import javafx.util.Duration; // För att använda Duration i animationen

public class Controller {
    // UI components
    @FXML
    private GridPane board;

    @FXML
    private ComboBox<String> modeSelection;

    @FXML
    private Label ComputerWins;

    @FXML
    private Label PlayerWins;

    @FXML
    private Label statusMessage;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;

    private Model model;

    // Constructor
    public Controller() {
        model = new Model();
    }

    // Initialization
    @FXML
    public void initialize() {
        resetBoard(); // Initial setup
        modeSelection.setValue("vs Computer"); // Set default mode
        handleModeSelection(); // Set up the game based on the default mode
        currentPlayerLabel.setText("Player : X");
    }

    // Button click handler with animation
    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (model.getGameStatus() != com.example.tictactoe6.GameStatus.ONGOING) {
            return; // Prevent any further actions if the game is not ongoing
        }

        // Get the button that was clicked
        Button clickedButton = (Button) event.getSource();

        // Lägg till animationen för knappen
        animateButtonClick(clickedButton); // Kalla på animation metoden

        int position = Integer.parseInt(clickedButton.getId().substring(6)) - 1; // Extract button number from its ID

        // Update the model with the player's move
        model.updateBoard(position);

        // Update the UI and status message after a move
        updateBoardUI();
        updateStatusMessage();

        // Check the game status after the move
        if (model.getGameStatus() != com.example.tictactoe6.GameStatus.ONGOING) {
            disableAllButtons(); // Disable all buttons if the game is over
        } else {
            // If the game is still ongoing, allow the computer to make its move if in vs Computer mode
            if (modeSelection.getValue().equals("vs Computer")) {
                computerMove(); // Call the computer move method
            }
        }
    }

    // Animation for button click
    private void animateButtonClick(Button button) {
        // Skapa en ScaleTransition som ändrar knappens storlek vid klick
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
        scale.setByX(0.2);  // Gör knappen 20% större horisontellt
        scale.setByY(0.2);  // Gör knappen 20% större vertikalt
        scale.setCycleCount(2);  // Antal gånger animationen körs (först växer knappen och sedan återgår den)
        scale.setAutoReverse(true);  // Återgår till ursprunglig storlek efter animation
        scale.play();  // Spela upp animationen
    }

    // Computer move handling with 2 seconds delay
    private void computerMove() {
        // Skapa en paus innan datorn gör sitt drag
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            // Här gör datorn sitt drag när pausen är över
            model.computerMove();
            updateBoardUI();
            updateStatusMessage();

            // Kolla om spelet är över efter datorns drag
            if (model.getGameStatus() != com.example.tictactoe6.GameStatus.ONGOING) {
                disableAllButtons();
            }
        });
        // Starta pausen
        pause.play();
    }

    // UI update methods
    private void updateBoardUI() {
        String[][] currentBoard = model.getBoard();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = (Button) board.getChildren().get(row * 3 + col);
                button.setText(currentBoard[row][col] != null ? currentBoard[row][col] : "");
                button.setDisable(currentBoard[row][col] != null); // Disable button if already clicked
            }
        }
    }

    private void updateStatusMessage() {
        com.example.tictactoe6.GameStatus currentStatus = model.checkGameStatus(); // Call the model's method
        statusMessage.setText(currentStatus.getMessage());
        updateWinsDisplay(); // Update the win count based on the current status
        currentPlayerLabel.setText("Current Player: " + model.getCurrentPlayer().getSymbol());
    }

    private void updateWinsDisplay() {
        PlayerWins.setText(String.valueOf(model.getPlayerXScore())); // Update Player X's score
        ComputerWins.setText(String.valueOf(model.getPlayerOScore())); // Update Player O's score
    }

    // Game reset and mode handling
    @FXML
    private void handleModeSelection() {
        model.resetGame(); // Reset game when mode changes
        resetBoard(); // Reset the UI board
        updateStatusMessage(); // Update the status message
    }

    @FXML
    private void resetBoard() {
        for (javafx.scene.Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText(""); // Clear button text
                button.setDisable(false); // Enable buttons
            }
        }
        model.resetBoard(); // Reset the model board
        updateStatusMessage(); // Update the initial status message
        currentPlayerLabel.setText("Current Player: X"); // Reset the current player label
    }

    @FXML
    private void resetGame() {
        model.resetGame();
        resetBoard();// Call the resetGame method from the model
        updateWinsDisplay(); // Update the UI to reflect reset scores
        updateStatusMessage(); // Optionally reset the status message
    }

    // Button disabling
    private void disableAllButtons() {
        for (javafx.scene.Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true); // Disable each button
            }
        }
    }
}
