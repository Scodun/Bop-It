package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.ButtonColor;

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

    public ButtonModel(ButtonModel source) {
        this.color = source.color;
        this.label = source.label;
    }
}
