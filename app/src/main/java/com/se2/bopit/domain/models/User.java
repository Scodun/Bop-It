package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable {

    private final String id;
    private final String name;
    private int score = 0;
    private int lives = 3;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void incrementScore() {
        score++;
    }

    public void loseLife() {
        lives--;
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

    public int getScore() {
        return score;
    }
}
