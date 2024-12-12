package com.example.tictactoe6;

public enum Players {
    PLAYER_X("X"),
    PLAYER_O("O"),
    PLAYER_CUSTOM("CUSTOM"); // Nytt alternativ för anpassad symbol

    private final String symbol;

    Players(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
