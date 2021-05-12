package com.se2.bopit.domain;

import java.util.List;

public class ImageButtonMinigameModel extends GameModel<ImageButtonModel>{

    public ImageButtonMinigameModel(String challenge, ImageButtonModel correctResponse, List<ImageButtonModel> wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }
}
