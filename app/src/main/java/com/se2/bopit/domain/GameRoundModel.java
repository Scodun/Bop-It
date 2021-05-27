package com.se2.bopit.domain;

public class GameRoundModel {
    public int round;

    public String currentUserId;

    public long time;

    public String gameType;

    public String modelType;

    public String modelJson;

    @Override
    public String toString() {
        return "GameRoundModel{" +
                "round=" + round +
                ", currentUserId='" + currentUserId + '\'' +
                ", time=" + time +
                ", gameType='" + gameType + '\'' +
                ", gameModel=" + modelJson +
                '}';
    }
}
