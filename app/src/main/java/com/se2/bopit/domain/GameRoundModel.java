package com.se2.bopit.domain;

public class GameRoundModel {
    private int round;

    private String currentUserId;

    private long time;

    private String gameType;

    private String modelType;

    private String modelJson;

    @Override
    public String toString() {
        return "GameRoundModel{" +
                "round=" + getRound() +
                ", currentUserId='" + getCurrentUserId() + '\'' +
                ", time=" + getTime() +
                ", gameType='" + getGameType() + '\'' +
                ", gameModel=" + getModelJson() +
                '}';
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelJson() {
        return modelJson;
    }

    public void setModelJson(String modelJson) {
        this.modelJson = modelJson;
    }
}
