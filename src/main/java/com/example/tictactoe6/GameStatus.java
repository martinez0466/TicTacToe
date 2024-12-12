package com.example.tictactoe6;

public enum GameStatus {
    ONGOING("Let's Start!"),
    X_WINS("Player X Wins!"),
    O_WINS("Player O Wins!"),
    DRAW("It's a Draw!");

    private final String message;

    GameStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

