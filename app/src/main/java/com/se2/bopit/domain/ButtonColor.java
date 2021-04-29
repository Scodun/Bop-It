package com.se2.bopit.domain;

public enum ButtonColor {
    RED,
    GREEN,
    BLUE,
    PURPLE,
    ORANGE,
    PINK,
    YELLOW,
    BLACK,
    // TODO add more colors and templates

    DEFAULT,
    RANDOM, // Random has to be last otherwise ButtonMiniGameFragment.getTintFromButtonColor may run forever
}
