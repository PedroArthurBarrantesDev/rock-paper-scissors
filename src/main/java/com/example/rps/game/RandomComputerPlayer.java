package com.example.rps.game;

import java.util.random.RandomGenerator;

import org.springframework.stereotype.Component;

/**
 * Picks Rock, Paper or Scissors with equal probability.
 */
@Component
public class RandomComputerPlayer implements ComputerPlayer {

    private static final Choice[] CHOICES = Choice.values();

    private final RandomGenerator random;

    public RandomComputerPlayer() {
        this(RandomGenerator.getDefault());
    }

    RandomComputerPlayer(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Choice choose() {
        return CHOICES[random.nextInt(CHOICES.length)];
    }
}
