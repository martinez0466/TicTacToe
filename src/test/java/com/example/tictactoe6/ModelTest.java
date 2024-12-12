package com.example.tictactoe6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    Model model = new Model();

    @Test
    @DisplayName("If a button is occupied, it cannot be clicked again")
    void ifButtonOccupiedButtonCannotBeClickedAgain() {
        model.updateBoard(0); // X
        model.updateBoard(0); // Invalid: Same spot
        model.updateBoard(1); // O
        model.updateBoard(1); // Invalid: Same spot

        assertEquals("X", model.getBoard()[0][0]);
        assertEquals("O", model.getBoard()[0][1]);
    }

    @Test
    @DisplayName("Player X wins and gets a point")
    void xWinsAndGetsPoint() {
        simulateMoves(0, 1, 3, 4, 6); // X wins

        assertEquals(GameStatus.X_WINS, model.checkGameStatus());
        assertEquals(1, model.getPlayerXScore());
        assertEquals(0, model.getPlayerOScore());
    }

    @Test
    @DisplayName("Player O wins and gets a point")
    void oWinsAndGetsPoint() {
        simulateMoves(0, 1, 3, 4, 8, 7); // O wins

        assertEquals(GameStatus.O_WINS, model.checkGameStatus());
        assertEquals(0, model.getPlayerXScore());
        assertEquals(1, model.getPlayerOScore());
    }

    @Test
    @DisplayName("Game ends in a draw and no points are awarded")
    void gameEndsInDraw() {
        simulateMoves(0, 1, 2, 4, 3, 5, 7, 6, 8); // Draw

        assertEquals(GameStatus.DRAW, model.checkGameStatus());
        assertEquals(0, model.getPlayerXScore());
        assertEquals(0, model.getPlayerOScore());
    }

    @Test
    @DisplayName("Game continues if no winner and board is not full")
    void gameContinuesIfNoWinner() {
        simulateMoves(0, 1, 2); // Board not full, no winner

        assertEquals(GameStatus.ONGOING, model.checkGameStatus());
    }

    @Test
    @DisplayName("checkWinner identifies the correct winner")
    void checkWinnerIdentifiesCorrectWinner() {
        // Horizontal win for X
        simulateMoves(0, 3, 1, 4, 2); // X wins
        assertEquals("X", model.checkWinner());

        // Vertical win for X
        resetAndSimulateMoves(0, 1, 3, 4, 6); // X wins
        assertEquals("X", model.checkWinner());

        // Diagonal win for X
        resetAndSimulateMoves(0, 1, 4, 2, 8); // X wins
        assertEquals("X", model.checkWinner());

        // No winner yet
        resetAndSimulateMoves(0, 1, 2); // No winner
        assertEquals("", model.checkWinner());
    }

    /**
     * Simulates a series of moves on the board.
     * @param moves The positions to update in sequence.
     */
    private void simulateMoves(int... moves) {
        for (int move : moves) {
            model.updateBoard(move);
        }
    }

    /**
     * Resets the board and simulates a series of moves.
     * @param moves The positions to update in sequence.
     */
    private void resetAndSimulateMoves(int... moves) {
        model.resetBoard();
        simulateMoves(moves);
    }
}
