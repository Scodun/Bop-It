package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.ButtonImage;

public class ImageButtonModel extends ResponseModel {

    public ButtonImage image;
    public String label;

    public ImageButtonModel(ButtonImage buttonImage) {
        this.image = buttonImage;
        this.label = image.name().toUpperCase();
    }

}
