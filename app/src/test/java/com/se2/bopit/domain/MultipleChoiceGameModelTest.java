package com.se2.bopit.domain;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MultipleChoiceGameModelTest {
    @Test
    public void shouldSetCorrectFlag() {
        MultipleChoiceGameModel<ButtonModel> gm = new ButtonMiniGameModel("test",
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
        MultipleChoiceGameModel<ButtonModel> gm = new ButtonMiniGameModel("test2",
                correct,
                new LinkedList<>(original));
        LinkedList<ButtonModel> wrongs = new LinkedList<>(gm.responses);
        wrongs.remove(correct);
        assertNotEquals(original, wrongs);
    }
}