package com.example.rps.game;

import java.util.Objects;

/**
 * Decides who wins a single round.
 */
public final class GameRules {

    private GameRules() {
    }

    public static Outcome decide(Choice player, Choice computer) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(computer, "computer");
        if (player == computer) {
            return Outcome.DRAW;
        }
        return player.beats(computer) ? Outcome.WIN : Outcome.LOSE;
    }
}
