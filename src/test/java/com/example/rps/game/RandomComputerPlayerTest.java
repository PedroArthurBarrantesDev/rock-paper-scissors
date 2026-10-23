package com.example.rps.game;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomComputerPlayerTest {

    @Test
    void picksEveryChoiceOverManyRounds() {
        RandomComputerPlayer computer = new RandomComputerPlayer(new Random(42));
        Set<Choice> seen = EnumSet.noneOf(Choice.class);

        for (int i = 0; i < 100; i++) {
            seen.add(computer.choose());
        }

        assertThat(seen).containsExactlyInAnyOrder(Choice.values());
    }

    @Test
    void neverReturnsNull() {
        RandomComputerPlayer computer = new RandomComputerPlayer();

        for (int i = 0; i < 100; i++) {
            assertThat(computer.choose()).isNotNull();
        }
    }
}
