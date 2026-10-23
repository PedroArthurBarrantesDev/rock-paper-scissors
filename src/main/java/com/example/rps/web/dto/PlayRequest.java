package com.example.rps.web.dto;

import com.example.rps.game.Choice;
import jakarta.validation.constraints.NotNull;

/**
 * The JSON body of a new round: {"choice": "ROCK"}.
 */
public record PlayRequest(@NotNull(message = "choice is required") Choice choice) {
}
