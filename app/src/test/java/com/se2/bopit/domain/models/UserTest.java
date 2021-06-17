package com.se2.bopit.domain.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    User user;

    @Before
    public void setup() {
        user = new User("id", "name");
    }

    @Test
    public void loseAllLives() {
        user.loseAllLives();

        assertEquals(0, user.getLives());
    }

    @Test
    public void setStartingLives() {
        User.setStartingLives(5);

        assertEquals(5, User.getStartingLives());
    }
}