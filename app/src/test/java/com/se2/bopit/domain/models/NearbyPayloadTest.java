package com.se2.bopit.domain.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NearbyPayloadTest {

    NearbyPayload payload;

    @Before
    public void setUp() throws Exception {
        payload = new NearbyPayload(0, "something");
    }

    @Test
    public void testToString() {
        String expected = "type=0; payload=something";

        String actual = payload.toString();

        assertEquals(expected, actual);
    }
}