package com.se2.bopit.domain;

import com.se2.bopit.domain.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    User user1;
    User user2;
    User user3;

    @Before
    public void setUp(){
        user1 = new User("A1","Paul",true);
        user2 = new User("A2","Sara", true);
        user3 = new User("A3", "Max", false);
    }

    @After
    public void tearDown(){
        user1 = null;
        user2 = null;
        user3 = null;
    }

    @Test
    public void testIncrementScore0(){

    }
}
