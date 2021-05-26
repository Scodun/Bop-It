package com.se2.bopit.domain.models;

import java.io.Serializable;

public class User implements Serializable{

    public String id;
    public String name;

    public User(String id, String name){
        this.id=id;
        this.name=name;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
