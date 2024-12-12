package com.example.tictactoe6;

import com.example.tictactoe6.GameStatus;
import com.example.tictactoe6.Players;
import java.util.Arrays;

public class Model {
    // Game State
    private String[][] board;
    private Players currentPlayer;
    private GameStatus gameStatus;

    // Player Scores
    private int playerXScore;
    private int playerOScore;

    // Constructor
    public Model() {
        resetGame(); // Ensure the game is initialized correctly
    }


    // ================== Spelstatus =================

    public GameStatus checkGameStatus() {
        String winner = checkWinner();
        if (!winner.isEmpty()) {
            return winner.equals("X") ? GameStatus.X_WINS : GameStatus.O_WINS;
        } else if (isBoardFull()) {
            return GameStatus.DRAW;
        }
        return GameStatus.ONGOING;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    // ================== Brädhanteirng ==================

    public void updateBoard(int position) {
        if (gameStatus != GameStatus.ONGOING) {
            return; // Prevent actions if the game is not ongoing
        }

        int row = position / 3;
        int col = position % 3;

        // Kontrollera om platsen är tom
        if (board[row][col] == null) {
            board[row][col] = currentPlayer.getSymbol();

            // Lägg till animation (glidande drag)
            animateMove(position, currentPlayer);

            // Kontrollera vinnaren
            String winner = checkWinner();
            if (!winner.isEmpty()) {
                if (winner.equals("X")) {
                    gameStatus = GameStatus.X_WINS;
                    playerXScore++; // Increment Player X's score
                } else {
                    gameStatus = GameStatus.O_WINS;
                    playerOScore++; // Increment Player O's score
                }
            } else if (isBoardFull()) {
                gameStatus = GameStatus.DRAW;
            } else {
                currentPlayer = (currentPlayer == Players.PLAYER_X) ? Players.PLAYER_O : Players.PLAYER_X;
            }
        }
    }

    // Kontrollerar om brädet är fullt
    public boolean isBoardFull() {
        for (String[] row : board) {
            for (String cell : row) {
                if (cell == null) {
                    return false;
                }
            }
        }
        return true;
    }

    // Kontrollera vinnare
    public String checkWinner() {
        for (int i = 0; i < 3; i++) {
            // Kontrollera rader och kolumner
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0];
            }
            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return board[0][i];
            }
        }
        // Kontrollera diagonaler
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0];
        }
        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2];
        }
        return ""; // No winner
    }

    // Metod för att få brädet (2D array)
    public String[][] getBoard() {
        return board;
    }

    // ================== Spelare Hantering ==================

    public Players getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPlayerXScore() {
        return playerXScore;
    }

    public int getPlayerOScore() {
        return playerOScore;
    }

    // ================= Omstart =================

    public void resetBoard() {
        board = new String[3][3]; // Återställ brädet
        currentPlayer = Players.PLAYER_X; // Återställ nuvarande spelare
        gameStatus = GameStatus.ONGOING; // Sätt spelet till igång
    }

    public void resetGame() {
        resetBoard();
        resetScores(); // Återställ poäng
        gameStatus = GameStatus.ONGOING;
    }

    public void resetScores() {
        playerXScore = 0;
        playerOScore = 0;
    }

    // ================= Animering och visuella effekter =================

    // Animering av drag (glidande drag)
    private void animateMove(int position, Players player) {
        int row = position / 3;
        int col = position % 3;

        try {
            Thread.sleep(500); // Vänta lite innan symbolen börjar "glida"
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Här simulerar vi att symbolen glider till den valda platsen
        // För enkelhetens skull gör vi ingen grafisk animation, men det kan göras med externa bibliotek
        board[row][col] = player.getSymbol(); // Placera symbolen på brädet
        System.out.println("Spelare " + player + " gör ett drag på position " + (position + 1));
    }

    // Markera vinnande positioner med färg
    public void markWinner() {
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horisontella
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertikala
                {0, 4, 8}, {2, 4, 6} // Diagonaler
        };

        // Färga de vinnande symbolerna (använder ANSI escape sekvenser för färg)
        for (int[] combination : winCombinations) {
            if (board[combination[0] / 3][combination[0] % 3].equals(board[combination[1] / 3][combination[1] % 3]) &&
                    board[combination[1] / 3][combination[1] % 3].equals(board[combination[2] / 3][combination[2] % 3]) &&
                    board[combination[0] / 3][combination[0] % 3] != null) {

                // Färga vinnande symboler i röd färg (eller annan färg du vill)
                for (int i : combination) {
                    int row = i / 3;
                    int col = i % 3;
                    board[row][col] = "\033[31m" + board[row][col] + "\033[0m"; // Röd färg för vinnande celler
                }

                printBoard();
                break; // Stoppa när vi har markerat vinnaren
            }
        }
    }

    // ================= Computer Move (AI) =================

    // En enkel AI för att göra draget när det är datorns tur (Player O)
    public void computerMove() {
        if (currentPlayer == Players.PLAYER_O && gameStatus == GameStatus.ONGOING) {
            // Basic logic: find the first available spot
            for (int i = 0; i < 9; i++) {
                int row = i / 3;
                int col = i % 3;
                if (board[row][col] == null) {
                    // Gör ett drag på första lediga platsen
                    board[row][col] = currentPlayer.getSymbol();

                    // Lägg till animation för datorns drag
                    animateMove(i, currentPlayer);

                    // Kontrollera status efter draget
                    String winner = checkWinner();
                    if (!winner.isEmpty()) {
                        if (winner.equals("X")) {
                            gameStatus = GameStatus.X_WINS;
                            playerXScore++; // Increment Player X's score
                        } else {
                            gameStatus = GameStatus.O_WINS;
                            playerOScore++; // Increment Player O's score
                        }
                    } else if (isBoardFull()) {
                        gameStatus = GameStatus.DRAW;
                    } else {
                        currentPlayer = Players.PLAYER_X; // Byt tillbaka till Player X
                    }
                    break; // Avsluta loopen när datorn har gjort sitt drag
                }
            }
        }
    }

    // Skriv ut brädet
    public void printBoard() {
        System.out.println("\nBräde:");
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            System.out.print(board[row][col] == null ? " " : board[row][col]);
            if (col < 2) {
                System.out.print(" | ");
            } else if (i < 6) {
                System.out.println("\n---------");
            }
        }
        System.out.println();
    }
}
