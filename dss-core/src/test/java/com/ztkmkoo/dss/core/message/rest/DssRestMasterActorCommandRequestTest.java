package com.ztkmkoo.dss.core.message.rest;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:45
 */
public class DssRestMasterActorCommandRequestTest {

    @Test
    public void testToString() {
        final ActorTestKit testKit = ActorTestKit.create();
        final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();

        final DssRestMasterActorCommandRequest request = DssRestMasterActorCommandRequest
                .builder()
                .channelId("TEST_CHANNEL_ID")
                .sender(probe.getRef())
                .methodType(DssRestMethodType.GET)
                .path("/test")
                .build();
        assertFalse(request.toString().isEmpty());
    }

    @Test
    public void getChannelId() {
        final ActorTestKit testKit = ActorTestKit.create();
        final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();

        final DssRestMasterActorCommandRequest request = DssRestMasterActorCommandRequest
                .builder()
                .channelId("hi")
                .sender(probe.getRef())
                .methodType(DssRestMethodType.GET)
                .path("/test")
                .build();
        assertEquals("hi", request.getChannelId());
    }

    @Test(expected = NullPointerException.class)
    public void getSender() {
        DssRestMasterActorCommandRequest.builder().sender(null).build();
    }
}