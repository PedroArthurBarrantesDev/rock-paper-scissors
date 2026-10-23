package com.example.rps.web.dto;

import com.example.rps.game.Match;
import com.example.rps.game.Outcome;

/**
 * The JSON view of a match: the score and, once it is over, the result for the player.
 */
public record MatchResponse(
        int playerWins,
        int computerWins,
        int draws,
        int roundsToWin,
        boolean over,
        Outcome result) {

    public static MatchResponse from(Match match) {
        return new MatchResponse(
                match.playerWins(),
                match.computerWins(),
                match.draws(),
                Match.ROUNDS_TO_WIN,
                match.isOver(),
                match.result().orElse(null));
    }
}
