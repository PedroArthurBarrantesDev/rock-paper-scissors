package com.example.rps.game;

/**
 * One played round: both moves and the outcome for the human player.
 */
public record Round(Choice player, Choice computer, Outcome outcome) {
}
