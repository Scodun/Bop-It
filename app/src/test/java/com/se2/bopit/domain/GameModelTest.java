package com.se2.bopit.domain;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameModelTest {
    @Test
    public void shouldSetCorrectFlag() {
        GameModel<ButtonModel> gm = new GameModel<>("test",
                new ButtonModel("correct"),
                new ButtonModel("wrong"));
        assertTrue(gm.responses.stream().filter(m -> m.label.equals("correct")).findAny().get().isCorrect);
        assertFalse(gm.responses.stream().filter(m -> m.label.equals("wrong")).findAny().get().isCorrect);
    }

    @Test
    public void shouldShuffleResponses() {
        List<ButtonModel> original = IntStream.range(1, 10)
                .mapToObj(i -> new ButtonModel("wrong" + i))
                .collect(Collectors.toList());
        ButtonModel correct = new ButtonModel("correct");
        GameModel<ButtonModel> gm = new GameModel<>("test2",
                correct,
                new LinkedList<>(original));
        LinkedList<ButtonModel> wrongs = new LinkedList<>(gm.responses);
        wrongs.remove(correct);
        assertNotEquals(original, wrongs);
    }
}