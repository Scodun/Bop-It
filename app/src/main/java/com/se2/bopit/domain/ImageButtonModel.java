package com.se2.bopit.domain;

public class ImageButtonModel extends ResponseModel{

    public ButtonImage image;
    public String label;

    public ImageButtonModel (ButtonImage buttonImage){
        this.image = buttonImage;
        this.label = image.name().toUpperCase();
    }

}
