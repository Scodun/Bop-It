package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.ButtonImage;

public class ImageButtonModel extends ResponseModel {

    private ButtonImage image;
    private String label;

    public ImageButtonModel(ButtonImage buttonImage) {
        this.setImage(buttonImage);
        this.setLabel(getImage().name().toUpperCase());
    }

    public ButtonImage getImage() {
        return image;
    }

    public void setImage(ButtonImage image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
