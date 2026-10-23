package com.example.rps.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A best-of-5 match: the first side to win 3 rounds wins the match. Draws don't count.
 */
public class Match implements Serializable {

    public static final int ROUNDS_TO_WIN = 3;

    private final List<Round> rounds = new ArrayList<>();
    private int playerWins;
    private int computerWins;
    private int draws;

    public Round play(Choice player, Choice computer) {
        if (isOver()) {
            throw new MatchOverException();
        }
        Round round = new Round(player, computer, GameRules.decide(player, computer));
        switch (round.outcome()) {
            case WIN -> playerWins++;
            case LOSE -> computerWins++;
            case DRAW -> draws++;
        }
        rounds.add(round);
        return round;
    }

    public boolean isOver() {
        return playerWins >= ROUNDS_TO_WIN || computerWins >= ROUNDS_TO_WIN;
    }

    /**
     * The match result for the human player, once the match is over.
     */
    public Optional<Outcome> result() {
        if (playerWins >= ROUNDS_TO_WIN) {
            return Optional.of(Outcome.WIN);
        }
        if (computerWins >= ROUNDS_TO_WIN) {
            return Optional.of(Outcome.LOSE);
        }
        return Optional.empty();
    }

    public int playerWins() {
        return playerWins;
    }

    public int computerWins() {
        return computerWins;
    }

    public int draws() {
        return draws;
    }

    public List<Round> rounds() {
        return List.copyOf(rounds);
    }
}
