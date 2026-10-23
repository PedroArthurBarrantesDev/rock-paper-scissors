package com.example.rps.web;

import com.example.rps.game.Match;
import com.example.rps.game.Round;
import com.example.rps.service.GameService;
import com.example.rps.web.dto.MatchResponse;
import com.example.rps.web.dto.PlayRequest;
import com.example.rps.web.dto.RoundResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for the current match and its rounds.
 */
@RestController
@RequestMapping("/api/match")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public MatchResponse currentMatch(HttpSession session) {
        return MatchResponse.from(gameService.currentMatch(session));
    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public RoundResponse playRound(@Valid @RequestBody PlayRequest request, HttpSession session) {
        Round round = gameService.playRound(session, request.choice());
        Match match = gameService.currentMatch(session);
        return RoundResponse.from(round, match);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse newMatch(HttpSession session) {
        return MatchResponse.from(gameService.newMatch(session));
    }
}
