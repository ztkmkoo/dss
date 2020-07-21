package io.github.ztkmkoo.dss.core.actor.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:57
 */
public class DssRestMasterActorTest extends AbstractDssActorTest {

    private static final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();
    private static final ActorRef<DssRestMasterActorCommand> restMasterActorRef = testKit.spawn(DssRestMasterActor.create(testServiceList()), "rest-master");

    @Test
    public void handlingDssRestMasterActorCommandRequest() {
        restMasterActorRef.tell(DssRestMasterActorCommandRequest
                .builder()
                .channelId("abcdefg")
                .sender(probe.ref())
                .methodType(DssRestMethodType.GET)
                .path("/test")
                .build());

        probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);
        assertTrue(true);
    }

    @Test
    public void handlingDssRestMasterActorCommandRequestErrorMethodType() {
        restMasterActorRef.tell(DssRestMasterActorCommandRequest
                .builder()
                .channelId("abcdefg")
                .sender(probe.ref())
                .methodType(DssRestMethodType.POST)
                .path("/test")
                .build());

        final DssRestChannelHandlerCommand command = probe.receiveMessage(Duration.ofSeconds(1));
        assertEquals(DssRestChannelHandlerCommandResponse.class, command.getClass());

        final DssRestChannelHandlerCommandResponse response = (DssRestChannelHandlerCommandResponse) command;
        assertNotNull(response);
        assertEquals("abcdefg", response.getChannelId());
        assertEquals(400, response.getStatus().intValue());
    }

    private static List<DssRestActorService> testServiceList() {
        final List<DssRestActorService> serviceList = new ArrayList<>();
        serviceList.add(new DssRestActorJsonService("hi", "/test", DssRestMethodType.GET) {

            @Override
            protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                return null;
            }
        });

        return serviceList;
    }
}