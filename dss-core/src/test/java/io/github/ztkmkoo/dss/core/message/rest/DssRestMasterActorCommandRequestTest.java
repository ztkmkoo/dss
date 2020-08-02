package io.github.ztkmkoo.dss.core.message.rest;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:45
 */
class DssRestMasterActorCommandRequestTest {

    @Test
    void testToString() {
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
    void getChannelId() {
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
    void getSender() {
        final DssRestMasterActorCommandRequest.DssRestMasterActorCommandRequestBuilder builder =
                DssRestMasterActorCommandRequest
                        .builder()
                        .sender(null);
        assertThrows(NullPointerException.class, builder::build);
    }
}