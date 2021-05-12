package com.se2.bopit.domain;

import java.util.List;

public class RightButtonCombinationModel extends GameModel<RightButtonModel> {

    public RightButtonModel secondCorrectResponse;

    public RightButtonCombinationModel(String challenge, RightButtonModel correctResponse,
                                       RightButtonModel secondCorrectResponse, List<RightButtonModel> wrongAnswers) {
        super(challenge, correctResponse, wrongAnswers);
        this.secondCorrectResponse = secondCorrectResponse;
    }
}
