package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable {

    private final String id;
    private final String name;
    private int score = 0;
    private boolean cheated;
    private int life = 3;
    private int currentRound = 0;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addScore() {
        score++;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean hasCheated() {
        return cheated;
    }

    public void setCheated(boolean cheated) {
        this.cheated = cheated;
    }

    public void looseLife() {
        life--;
    }

    public int getLife() {
        return life;
    }

    public void setCurrentRound(int round){
        this.currentRound = round;
    }
    public int getCurrentRound(){
        return currentRound;
    }
}
