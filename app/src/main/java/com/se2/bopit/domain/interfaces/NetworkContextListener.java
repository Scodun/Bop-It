package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.models.User;

import java.util.ArrayList;

public interface NetworkContextListener {
    void onGameStart(ArrayList<User> users);
}
