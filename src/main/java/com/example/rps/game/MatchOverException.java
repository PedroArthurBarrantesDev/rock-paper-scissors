package com.example.rps.game;

/**
 * Thrown when a round is played after the match already has a winner.
 */
public class MatchOverException extends IllegalStateException {

    public MatchOverException() {
        super("The match is over; start a new one");
    }
}
