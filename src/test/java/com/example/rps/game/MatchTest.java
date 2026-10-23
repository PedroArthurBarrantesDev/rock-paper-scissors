package com.example.rps.game;

import org.junit.jupiter.api.Test;

import static com.example.rps.game.Choice.PAPER;
import static com.example.rps.game.Choice.ROCK;
import static com.example.rps.game.Choice.SCISSORS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class MatchTest {

    @Test
    void newMatchIsNotOver() {
        Match match = new Match();

        assertThat(match.isOver()).isFalse();
        assertThat(match.result()).isEmpty();
        assertThat(match.rounds()).isEmpty();
    }

    @Test
    void playerWinsAfterThreeRoundWins() {
        Match match = new Match();

        match.play(ROCK, SCISSORS);
        match.play(PAPER, ROCK);
        assertThat(match.isOver()).isFalse();
        match.play(SCISSORS, PAPER);

        assertThat(match.isOver()).isTrue();
        assertThat(match.result()).contains(Outcome.WIN);
        assertThat(match.playerWins()).isEqualTo(3);
        assertThat(match.computerWins()).isZero();
    }

    @Test
    void computerWinsAfterThreeRoundWins() {
        Match match = new Match();

        match.play(ROCK, PAPER);
        match.play(ROCK, SCISSORS);
        match.play(ROCK, PAPER);
        match.play(ROCK, PAPER);

        assertThat(match.result()).contains(Outcome.LOSE);
        assertThat(match.playerWins()).isEqualTo(1);
        assertThat(match.computerWins()).isEqualTo(3);
    }

    @Test
    void drawsDoNotCountTowardWinning() {
        Match match = new Match();

        for (int i = 0; i < 10; i++) {
            match.play(ROCK, ROCK);
        }

        assertThat(match.isOver()).isFalse();
        assertThat(match.draws()).isEqualTo(10);
        assertThat(match.rounds()).hasSize(10);
    }

    @Test
    void recordsEachRound() {
        Match match = new Match();

        Round round = match.play(PAPER, SCISSORS);

        assertThat(round).isEqualTo(new Round(PAPER, SCISSORS, Outcome.LOSE));
        assertThat(match.rounds()).containsExactly(round);
    }

    @Test
    void cannotPlayAfterTheMatchIsOver() {
        Match match = new Match();
        match.play(ROCK, SCISSORS);
        match.play(ROCK, SCISSORS);
        match.play(ROCK, SCISSORS);

        assertThatIllegalStateException().isThrownBy(() -> match.play(ROCK, SCISSORS));
    }
}
