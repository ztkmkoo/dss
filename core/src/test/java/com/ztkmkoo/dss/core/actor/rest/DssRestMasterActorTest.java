package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        serviceList.add(new DssRestActorService() {
            @Override
            public String getName() {
                return "hi";
            }

            @Override
            public String getPath() {
                return "/test";
            }

            @Override
            public DssRestMethodType getMethodType() {
                return DssRestMethodType.GET;
            }

            @Override
            public DssRestServiceResponse handling(DssRestServiceRequest request) {
                return null;
            }
        });

        return serviceList;
    }
}