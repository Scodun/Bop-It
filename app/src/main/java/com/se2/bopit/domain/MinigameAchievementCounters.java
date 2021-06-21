package com.se2.bopit.domain;

public class MinigameAchievementCounters {

    private MinigameAchievementCounters() {
    }

    private static int counterImageButtonMinigame = 0;
    private static int counterColorButtonMinigame = 0;
    private static int counterShakePhoneMinigame = 0;
    private static int counterTextBasedMinigame = 0;
    private static int counterPlacePhoneMinigame = 0;
    private static int counterRightButtonsMinigame = 0;
    private static int counterCoverLightSensorMinigame = 0;
    private static int counterSliderMinigame = 0;
    private static int counterVolumeButtonMinigame = 0;
    private static int counterDrawingMinigame = 0;
    private static int counterSpecialTextButtonMinigame = 0;

    public static int getImageButtonMinigameCounter() {
        return getCounterImageButtonMinigame();
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

    public static int getCounterSpecialTextButtonMinigame() { return counterSpecialTextButtonMinigame; }

    public static void resetCounter() {
        setCounterImageButtonMinigame(0);
        setCounterColorButtonMinigame(0);
        setCounterShakePhoneMinigame(0);
        setCounterTextBasedMinigame(0);
        setCounterPlacePhoneMinigame(0);
        setCounterRightButtonsMinigame(0);
        setCounterCoverLightSensorMinigame(0);
        setCounterSliderMinigame(0);
        setCounterVolumeButtonMinigame(0);
        setCounterDrawingMinigame(0);
        setCounterSpecialTextButtonMinigame(0);
    }

    public static int getCounterImageButtonMinigame() {
        return counterImageButtonMinigame;
    }

    public static void setCounterImageButtonMinigame(int counterImageButtonMinigame) {
        MinigameAchievementCounters.counterImageButtonMinigame = counterImageButtonMinigame;
    }

    public static void setCounterColorButtonMinigame(int counterColorButtonMinigame) {
        MinigameAchievementCounters.counterColorButtonMinigame = counterColorButtonMinigame;
    }

    public static void setCounterShakePhoneMinigame(int counterShakePhoneMinigame) {
        MinigameAchievementCounters.counterShakePhoneMinigame = counterShakePhoneMinigame;
    }

    public static void setCounterTextBasedMinigame(int counterTextBasedMinigame) {
        MinigameAchievementCounters.counterTextBasedMinigame = counterTextBasedMinigame;
    }

    public static void setCounterPlacePhoneMinigame(int counterPlacePhoneMinigame) {
        MinigameAchievementCounters.counterPlacePhoneMinigame = counterPlacePhoneMinigame;
    }

    public static void setCounterRightButtonsMinigame(int counterRightButtonsMinigame) {
        MinigameAchievementCounters.counterRightButtonsMinigame = counterRightButtonsMinigame;
    }

    public static void setCounterCoverLightSensorMinigame(int counterCoverLightSensorMinigame) {
        MinigameAchievementCounters.counterCoverLightSensorMinigame = counterCoverLightSensorMinigame;
    }

    public static void setCounterSliderMinigame(int counterSliderMinigame) {
        MinigameAchievementCounters.counterSliderMinigame = counterSliderMinigame;
    }

    public static void setCounterVolumeButtonMinigame(int counterVolumeButtonMinigame) {
        MinigameAchievementCounters.counterVolumeButtonMinigame = counterVolumeButtonMinigame;
    }

    public static void setCounterDrawingMinigame(int counterDrawingMinigame) {
        MinigameAchievementCounters.counterDrawingMinigame = counterDrawingMinigame;
    }

    public static void setCounterSpecialTextButtonMinigame(int counterSpecialTextButtonMinigame) {
        MinigameAchievementCounters.counterSpecialTextButtonMinigame = counterSpecialTextButtonMinigame;
    }
}
