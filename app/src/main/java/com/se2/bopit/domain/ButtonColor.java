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

    DEFAULT,
    RANDOM, // Random has to be last otherwise ButtonMiniGameFragment.getTintFromButtonColor may run forever
}
