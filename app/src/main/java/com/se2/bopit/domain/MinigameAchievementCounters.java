package com.se2.bopit.domain;

public class MinigameAchievementCounters {
    public static int counterImageButtonMinigame = 0;
    public static int counterColorButtonMinigame = 0;
    public static int counterShakePhoneMinigame = 0;
    public static int counterTextBasedMinigame = 0;
    public static int counterPlacePhoneMinigame = 0;
    public static int counterRightButtonsMinigame = 0;
    public static int counterCoverLightSensorMinigame = 0;
    public static int counterSliderMinigame = 0;
    public static int counterVolumeButtonMinigame = 0;
    public static int counterDrawingMinigame = 0;

    public static int getImageButtonMinigameCounter() {
        return counterImageButtonMinigame;
    }

    public static int getCounterColorButtonMinigame() {
        return counterColorButtonMinigame;
    }

    public static int getCounterShakePhoneMinigame() {
        return counterShakePhoneMinigame;
    }

    public static int getCounterTextBasedMinigame() {
        return counterTextBasedMinigame;
    }

    public static int getCounterPlacePhoneMinigame() {
        return counterPlacePhoneMinigame;
    }

    public static int getCounterRightButtonsMinigame() {
        return counterRightButtonsMinigame;
    }

    public static int getCounterCoverLightSensorMinigame() {
        return counterCoverLightSensorMinigame;
    }

    public static int getCounterSliderMinigame() {
        return counterSliderMinigame;
    }

    public static int getCounterVolumeButtonMinigame() {
        return counterVolumeButtonMinigame;
    }

    public static int getCounterDrawingMinigame() {
        return counterDrawingMinigame;
    }

    public static void resetCounter() {
        counterImageButtonMinigame = 0;
        counterColorButtonMinigame = 0;
        counterShakePhoneMinigame = 0;
        counterTextBasedMinigame = 0;
        counterPlacePhoneMinigame = 0;
        counterRightButtonsMinigame = 0;
        counterCoverLightSensorMinigame = 0;
        counterSliderMinigame = 0;
        counterVolumeButtonMinigame = 0;
        counterDrawingMinigame = 0;
    }
}
