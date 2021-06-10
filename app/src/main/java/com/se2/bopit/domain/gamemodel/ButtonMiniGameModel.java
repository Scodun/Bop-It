package com.se2.bopit.domain.gamemodel;

import com.se2.bopit.domain.responsemodel.ButtonModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ButtonMiniGameModel extends MultipleChoiceGameModel<ButtonModel> {

    public ButtonMiniGameModel(String challenge, ButtonModel correctResponse, ButtonModel... wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }

    public ButtonMiniGameModel(String challenge, ButtonModel correctResponse, List<ButtonModel> wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }

    /**
     * Randomly picks a number of answers from a list of possible answers to create the GameModel
     *
     * @param possibleAnswers List of possible answers
     * @param numberAnswers   Number of answers to randomly choose from the list.
     *                        One of them will be correct.
     * @return GameModel with 1 correct response and possibleAnswers-1 incorrect responses
     */
    public static ButtonMiniGameModel createRandomGameModel(List<ButtonModel> possibleAnswers, int numberAnswers) {
        ArrayList<ButtonModel> possibleAnswersCopy = new ArrayList<>(possibleAnswers);

        Collections.shuffle(possibleAnswersCopy);

        ButtonModel correctResponse = new ButtonModel(possibleAnswersCopy.get(0));

        ArrayList<ButtonModel> wrongResponses = new ArrayList<>();
        for (int i = 1; i < numberAnswers; i++) {
            ButtonModel wrongResponse = new ButtonModel(possibleAnswersCopy.get(i));
            wrongResponses.add(wrongResponse);
        }

        return new ButtonMiniGameModel(
                String.format("Select %s!", correctResponse.label),
                correctResponse,
                wrongResponses
        );
    }

}
