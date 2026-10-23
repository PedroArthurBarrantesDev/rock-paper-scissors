package com.example.rps.game;

import java.io.Serializable;

/**
 * One played round: both moves and the outcome for the human player.
 */
public record Round(Choice player, Choice computer, Outcome outcome) implements Serializable {
}
