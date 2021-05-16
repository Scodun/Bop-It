package com.se2.bopit.domain;

import com.se2.bopit.domain.interfaces.GameListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class MultipleChoiceGameModelTest {

    MultipleChoiceGameModel<ButtonModel> gameModel;
    ButtonModel correct;
    ButtonModel[] wrongs;
    private GameListener gameListenerMock;

    @Before
    public void setUp() {
        correct = new ButtonModel("correct");
        wrongs = new ButtonModel[3];
        for(int i = 0; i < 3; i++) {
            wrongs[i] = new ButtonModel("wrong" + i);
        }
        gameModel = new ButtonMiniGameModel("test", correct, wrongs);
        gameListenerMock = mock(GameListener.class);
        gameModel.setGameListener(gameListenerMock);
    }

    @After
    public void tearDown() {
        reset(gameListenerMock);
    }

    @Test
    public void shouldSetCorrectFlag() {
        assertTrue(gameModel.responses.stream()
                .filter(m -> m == correct)
                .allMatch(m -> m.isCorrect));
        assertTrue(gameModel.responses.stream()
                .filter(m -> m.label.startsWith("wrong"))
                .noneMatch(m -> m.isCorrect));
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

    @Test
    public void handleCorrectResponse() {

        assertTrue(gameModel.handleResponse(correct));
        verify(gameListenerMock).onGameResult(eq(true));

    }

    @Test
    public void handleWrongResponse() {
        assertFalse(gameModel.handleResponse(wrongs[0]));
        verify(gameListenerMock).onGameResult(eq(false));
    }
}