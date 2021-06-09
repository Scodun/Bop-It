package com.se2.bopit.domain.models;

public class UserListViewItem {
    private String username;
    private boolean ready = false;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public UserListViewItem(String username, boolean ready) {
        this.username = username;
        this.ready = ready;
    }
}
