package com.example.rps.service;

import com.example.rps.game.Choice;
import com.example.rps.game.ComputerPlayer;
import com.example.rps.game.Match;
import com.example.rps.game.Round;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

/**
 * Keeps one match per browser session and plays rounds against the computer.
 */
@Service
public class GameService {

    static final String MATCH_ATTRIBUTE = "match";

    private final ComputerPlayer computerPlayer;

    public GameService(ComputerPlayer computerPlayer) {
        this.computerPlayer = computerPlayer;
    }

    /**
     * Returns this session's match, starting one if there is none yet.
     */
    public Match currentMatch(HttpSession session) {
        Match match = (Match) session.getAttribute(MATCH_ATTRIBUTE);
        if (match == null) {
            match = newMatch(session);
        }
        return match;
    }

    public Round playRound(HttpSession session, Choice playerChoice) {
        Match match = currentMatch(session);
        synchronized (match) {
            Round round = match.play(playerChoice, computerPlayer.choose());
            // Store it again so session replication sees the change
            session.setAttribute(MATCH_ATTRIBUTE, match);
            return round;
        }
    }

    public Match newMatch(HttpSession session) {
        Match match = new Match();
        session.setAttribute(MATCH_ATTRIBUTE, match);
        return match;
    }
}
