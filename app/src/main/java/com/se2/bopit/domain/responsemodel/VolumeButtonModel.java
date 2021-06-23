package com.se2.bopit.domain.responsemodel;

import com.se2.bopit.domain.VolumeButton;

public class VolumeButtonModel extends ResponseModel {

    private VolumeButton volumeButton;
    private String label;

    public VolumeButtonModel(VolumeButton volumeButton) {
        this.setVolumeButton(volumeButton);
        this.setLabel(volumeButton.name().toUpperCase());
    }

    public void setVolumeButton(VolumeButton volumeButton) {
        this.volumeButton = volumeButton;
        this.setLabel(volumeButton.name().toUpperCase());
    }

    public VolumeButton getVolumeButton() {
        return volumeButton;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
