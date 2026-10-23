package com.example.rps.game;

/**
 * A move a player can make.
 */
public enum Choice {
    ROCK,
    PAPER,
    SCISSORS;

    /**
     * Returns true when this choice beats the other one.
     */
    public boolean beats(Choice other) {
        return switch (this) {
            case ROCK -> other == SCISSORS;
            case PAPER -> other == ROCK;
            case SCISSORS -> other == PAPER;
        };
    }
}
