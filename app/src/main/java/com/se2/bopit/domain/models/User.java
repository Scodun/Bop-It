package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable {

    private final String id;
    private final String name;
    private int score = 0;
    private boolean ready = false;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(String id, String name, boolean ready) {
        this.id = id;
        this.name = name;
        this.ready = ready;
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

    public boolean isReady(){return ready;}

    public int getScore() {
        return score;
    }

    public void setReady(boolean ready){
        this.ready = ready;
    }
}
