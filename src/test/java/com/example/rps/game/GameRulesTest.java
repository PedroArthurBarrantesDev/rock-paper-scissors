package com.example.rps.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class GameRulesTest {

    @ParameterizedTest(name = "{0} vs {1} -> {2}")
    @CsvSource({
            "ROCK,     ROCK,     DRAW",
            "ROCK,     PAPER,    LOSE",
            "ROCK,     SCISSORS, WIN",
            "PAPER,    ROCK,     WIN",
            "PAPER,    PAPER,    DRAW",
            "PAPER,    SCISSORS, LOSE",
            "SCISSORS, ROCK,     LOSE",
            "SCISSORS, PAPER,    WIN",
            "SCISSORS, SCISSORS, DRAW"
    })
    void decidesEveryMatchup(Choice player, Choice computer, Outcome expected) {
        assertThat(GameRules.decide(player, computer)).isEqualTo(expected);
    }

    @Test
    void rejectsMissingChoices() {
        assertThatNullPointerException().isThrownBy(() -> GameRules.decide(null, Choice.ROCK));
        assertThatNullPointerException().isThrownBy(() -> GameRules.decide(Choice.ROCK, null));
    }
}
