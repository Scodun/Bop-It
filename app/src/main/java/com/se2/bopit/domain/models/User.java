package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable {

    private final String id;
    private final String name;
    private int score = 0;
    private boolean cheated;
    private boolean cheatDetected;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        cheated = false;
        cheatDetected = false;
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

    public boolean hasCheated(){
        return cheated;
    }

    public void setCheated(boolean cheated){
        this.cheated = cheated;
    }

    public boolean isCheatDetected() {
        return cheatDetected;
    }

    public void setCheatDetected(boolean cheatDetected){
        this.cheatDetected = cheatDetected;
    }
}
