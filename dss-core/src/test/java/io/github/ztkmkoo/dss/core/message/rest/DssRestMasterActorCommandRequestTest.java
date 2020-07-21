package io.github.ztkmkoo.dss.core.message.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

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

    @Test
    public void getSender() {
        assertThrows(NullPointerException.class,
            () -> DssRestMasterActorCommandRequest
                .builder()
                .sender(null)
                .build());
    }
}