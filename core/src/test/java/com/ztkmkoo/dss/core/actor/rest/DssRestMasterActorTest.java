package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:57
 */
public class DssRestMasterActorTest extends AbstractDssActorTest {

    @Test
    public void handlingDssRestMasterActorCommandRequest() {

        final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();
        final ActorRef<DssRestMasterActorCommand> restMasterActorRef = testKit.spawn(DssRestMasterActor.create(Collections.emptyList()), "rest-master");

        restMasterActorRef.tell(DssRestMasterActorCommandRequest
                .builder()
                .channelId("abcdefg")
                .sender(probe.ref())
                .build());

        probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);
        assertTrue(true);
    }
}