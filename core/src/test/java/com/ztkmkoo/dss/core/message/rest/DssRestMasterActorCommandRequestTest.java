package com.ztkmkoo.dss.core.message.rest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:45
 */
public class DssRestMasterActorCommandRequestTest {

    @Test
    public void testToString() {
        final DssRestMasterActorCommandRequest request = DssRestMasterActorCommandRequest.builder().build();
        assertFalse(request.toString().isEmpty());
    }

    @Test
    public void getChannelId() {
        final DssRestMasterActorCommandRequest request = DssRestMasterActorCommandRequest.builder().channelId("hi").build();
        assertEquals("hi", request.getChannelId());
    }

    @Test
    public void getSender() {
        final DssRestMasterActorCommandRequest request = DssRestMasterActorCommandRequest.builder().sender(null).build();
        assertNull(request.getSender());
    }
}