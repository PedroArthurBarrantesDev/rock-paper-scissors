package com.example.rps.web.dto;

import com.example.rps.game.Match;
import com.example.rps.game.Round;

/**
 * The JSON answer to a played round: both moves, the outcome, and the match after it.
 */
public record RoundResponse(Round round, MatchResponse match) {

    public static RoundResponse from(Round round, Match match) {
        return new RoundResponse(round, MatchResponse.from(match));
    }
}
