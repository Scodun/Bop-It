package com.se2.bopit.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable, Parcelable, Comparable<User> {

    private static int startingLives = 3;

    private final String id;
    private final String name;
    private int score = 0;

    private boolean cheated = false;

    private int lives;
    private boolean ready;

    public User(String id, String name) {
        this(id, name, false);
    }

    public User(String id, String name, boolean ready) {
        this.id = id;
        this.name = name;
        this.ready = ready;
        lives = startingLives;
    }

    public void incrementScore() {
        score++;
    }

    public void loseLife() {
        lives--;
    }

    public void loseAllLives() {
        lives = 0;
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

    public boolean isReady() {
        return ready;
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

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public static int getStartingLives() {
        return startingLives;
    }

    public static void setStartingLives(int startingLives) {
        User.startingLives = startingLives;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(score);
    }

    public User(Parcel in) {
        id = in.readString();
        name = in.readString();
        score = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int compareTo(User user) {
        return user.getScore() - this.score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User)
            return ((User) obj).getId().equals(this.id);
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

}
