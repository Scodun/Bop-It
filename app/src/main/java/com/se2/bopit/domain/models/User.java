package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable{

    private String id;
    private String name;
    private int score = 0;

    public User(String id, String name){
        this.id=id;
        this.name=name;
    }

    public void addScore(){
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
}
