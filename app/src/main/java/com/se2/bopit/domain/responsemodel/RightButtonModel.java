package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.RightButton;

public class RightButtonModel extends ResponseModel {

    private RightButton rightButton;
    private String label;

    public RightButtonModel(RightButton rightButton) {
        this.setRightButton(rightButton);
        this.setLabel(rightButton.name().toUpperCase());
    }

    public RightButton getRightButton() {
        return rightButton;
    }

    public void setRightButton(RightButton rightButton) {
        this.rightButton = rightButton;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
