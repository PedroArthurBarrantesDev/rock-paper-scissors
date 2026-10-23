package com.example.rps.service;

import com.example.rps.game.Choice;
import com.example.rps.game.Match;
import com.example.rps.game.Outcome;
import com.example.rps.game.Round;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class GameServiceTest {

    private final GameService gameService = new GameService(() -> Choice.SCISSORS);

    @Test
    void startsAMatchOnFirstUse() {
        MockHttpSession session = new MockHttpSession();

        Match match = gameService.currentMatch(session);

        assertThat(match.rounds()).isEmpty();
        assertThat(gameService.currentMatch(session)).isSameAs(match);
    }

    @Test
    void playsARoundAgainstTheComputer() {
        MockHttpSession session = new MockHttpSession();

        Round round = gameService.playRound(session, Choice.ROCK);

        assertThat(round).isEqualTo(new Round(Choice.ROCK, Choice.SCISSORS, Outcome.WIN));
        assertThat(gameService.currentMatch(session).playerWins()).isEqualTo(1);
    }

    @Test
    void keepsSeparateMatchesPerSession() {
        MockHttpSession first = new MockHttpSession();
        MockHttpSession second = new MockHttpSession();

        gameService.playRound(first, Choice.ROCK);

        assertThat(gameService.currentMatch(first).playerWins()).isEqualTo(1);
        assertThat(gameService.currentMatch(second).playerWins()).isZero();
    }

    @Test
    void newMatchResetsTheScore() {
        MockHttpSession session = new MockHttpSession();
        gameService.playRound(session, Choice.ROCK);

        Match match = gameService.newMatch(session);

        assertThat(match.playerWins()).isZero();
        assertThat(gameService.currentMatch(session)).isSameAs(match);
    }
}
