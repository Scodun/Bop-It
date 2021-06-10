package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable {
    public static int STARTING_LIVES = 3;

    private final String id;
    private final String name;
    private int score = 0;

    private boolean cheated = false;
    private int life = 3;
    private int currentRound = 0;

    private int lives;
    private boolean ready;

    public User(String id, String name) {
        this(id, name, false);
    }
    public User(String id, String name, boolean ready) {
        this.id = id;
        this.name = name;
        this.ready = ready;
        lives = STARTING_LIVES;
    }

    public void incrementScore() {
        score++;
    }

    public void loseLife() {
        lives--;
    }

    public void loseAllLifes() {
        lives=0;
    }

    public String getId() {
        return id;
    }

    public int getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }

    public boolean isReady(){return ready;}

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

    public void setReady(boolean ready){
        this.ready = ready;
    }
}
