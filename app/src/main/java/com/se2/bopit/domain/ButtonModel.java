package com.se2.bopit.domain;

public class ButtonModel extends ResponseModel {
    public final int color;
    public final String label;

    public ButtonModel(int color) {
        this(color, null);
    }

    public ButtonModel(String label) {
        this(0, label);
    }

    public ButtonModel(int color, String label) {
        this.color = color;
        this.label = label;
    }
}
