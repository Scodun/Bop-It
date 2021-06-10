package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.RightButton;

public class RightButtonModel extends ResponseModel {

    public RightButton rightButton;
    public String label;

    public RightButtonModel(RightButton rightButton) {
        this.rightButton = rightButton;
        this.label = rightButton.name().toUpperCase();
    }
}
