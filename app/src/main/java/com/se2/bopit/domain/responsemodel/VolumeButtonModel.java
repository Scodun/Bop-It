package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.VolumeButton;

public class VolumeButtonModel extends ResponseModel {

    public VolumeButton volumeButton;
    public String label;

    public VolumeButtonModel(VolumeButton volumeButton) {
        this.volumeButton = volumeButton;
        this.label = volumeButton.name().toUpperCase();
    }

    public void setVolumeButton(VolumeButton volumeButton) {
        this.volumeButton = volumeButton;
        this.label = volumeButton.name().toUpperCase();
    }
}
