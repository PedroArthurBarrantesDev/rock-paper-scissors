package com.example.rps.web;

import com.example.rps.game.Choice;
import com.example.rps.game.ComputerPlayer;
import com.example.rps.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@Import(GameService.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComputerPlayer computerPlayer;

    private final MockHttpSession session = new MockHttpSession();

    @BeforeEach
    void computerAlwaysPicksScissors() {
        when(computerPlayer.choose()).thenReturn(Choice.SCISSORS);
    }

    @Test
    void getReturnsAFreshMatch() throws Exception {
        mockMvc.perform(get("/api/match").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerWins").value(0))
                .andExpect(jsonPath("$.computerWins").value(0))
                .andExpect(jsonPath("$.draws").value(0))
                .andExpect(jsonPath("$.roundsToWin").value(3))
                .andExpect(jsonPath("$.over").value(false))
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @Test
    void playingARoundReturnsCreatedWithTheResult() throws Exception {
        playRound("ROCK")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.round.player").value("ROCK"))
                .andExpect(jsonPath("$.round.computer").value("SCISSORS"))
                .andExpect(jsonPath("$.round.outcome").value("WIN"))
                .andExpect(jsonPath("$.match.playerWins").value(1))
                .andExpect(jsonPath("$.match.over").value(false));
    }

    @Test
    void threeWinsEndTheMatch() throws Exception {
        playRound("ROCK");
        playRound("ROCK");
        playRound("ROCK")
                .andExpect(jsonPath("$.match.over").value(true))
                .andExpect(jsonPath("$.match.result").value("WIN"));

        mockMvc.perform(get("/api/match").session(session))
                .andExpect(jsonPath("$.playerWins").value(3))
                .andExpect(jsonPath("$.result").value("WIN"));
    }

    @Test
    void playingAfterTheMatchIsOverIsAConflict() throws Exception {
        playRound("ROCK");
        playRound("ROCK");
        playRound("ROCK");

        playRound("ROCK")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("The match is over; start a new one"));
    }

    @Test
    void unknownChoiceIsABadRequest() throws Exception {
        playRound("LIZARD")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("ROCK, PAPER, SCISSORS")));
    }

    @Test
    void missingChoiceIsABadRequest() throws Exception {
        mockMvc.perform(post("/api/match/rounds").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("choice is required")));
    }

    @Test
    void newMatchResetsTheScore() throws Exception {
        playRound("ROCK");

        mockMvc.perform(post("/api/match").session(session))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerWins").value(0));

        mockMvc.perform(get("/api/match").session(session))
                .andExpect(jsonPath("$.playerWins").value(0));
    }

    private ResultActions playRound(String choice) throws Exception {
        return mockMvc.perform(post("/api/match/rounds").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"choice\":\"" + choice + "\"}"));
    }
}
