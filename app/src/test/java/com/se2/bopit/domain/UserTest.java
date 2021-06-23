package com.se2.bopit.domain;

import com.se2.bopit.domain.models.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    User user1;
    User user2;

    @Before
    public void setUp() {
        user1 = new User("A1", "Paul", true);
        user2 = new User("A2", "Sara", false);
    }

    @After
    public void tearDown() {
        user1 = null;
        user2 = null;
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("Paul", user1.getName());
    }

    @Test
    public void testIsReadyTrue() {
        Assert.assertTrue(user1.isReady());
    }

    @Test
    public void testIsReadyFalse() {
        Assert.assertFalse(user2.isReady());
    }

    @Test
    public void testLoseAllLives() {
        user1.loseAllLives();
        Assert.assertEquals(0, user1.getLives());
    }

    @Test
    public void testIncrementScoreOnce() {
        user1.incrementScore();
        Assert.assertEquals(1, user1.getScore());
    }

    @Test
    public void testIncrementScoreTwice() {
        user1.incrementScore();
        user1.incrementScore();
        Assert.assertEquals(2, user1.getScore());
    }

    @Test
    public void testHasCheatedTrue() {
        user1.setCheated(true);
        Assert.assertTrue(user1.hasCheated());
    }

    @Test
    public void testHasCheatedFalse() {
        user1.setReady(true);
        user1.setCheated(false);
        Assert.assertFalse(user1.hasCheated());
    }

    @Test
    public void testSetReadyFalse() {
        user1.setReady(false);
        Assert.assertFalse(user1.isReady());
    }

    @Test
    public void testSetReadyTrue() {
        user2.setReady(true);
        Assert.assertTrue(user2.isReady());
    }
}
