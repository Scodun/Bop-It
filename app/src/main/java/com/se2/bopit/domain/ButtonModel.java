package com.se2.bopit.domain;

public class ButtonModel extends ResponseModel {
    public final ButtonColor color;
    public final String label;

    public ButtonModel(ButtonColor color) {
        this(color, color.name());
    }

    public ButtonModel(String label) {
        this(ButtonColor.DEFAULT, label);
    }

    public ButtonModel(ButtonColor color, String label) {
        this.color = color;
        this.label = label;
    }
    
    @Override
    public ButtonModel clone() {
        return new ButtonModel(color, label);
    }
}
