package com.se2.bopit.domain;

public class RightButtonModel extends ResponseModel{

    public RightButton rightButton;
    public String label;

    public RightButtonModel(RightButton rightButton){
        this.rightButton = rightButton;
        this.label = rightButton.name().toUpperCase();
    }
}
