package com.ztkmkoo.dss.server.actor.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.AskPattern;
import com.ztkmkoo.dss.server.actor.AbstractActorTest;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.message.ServerMessages;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 1:08
 */
public class DssServerMasterActorTest extends AbstractActorTest<DssServerMasterActor> {

    @Override
    protected Class<DssServerMasterActor> getActorClass() {
        return DssServerMasterActor.class;
    }

    @Test
    public void create() {

        final ActorRef<SpawnProtocol.Command> master = testKit.spawn(DssServerMasterActor.create());
        CompletionStage<ActorRef<ServerMessages.Req>> cs = AskPattern
                .ask(
                        master,
                        DssNetworkType.HTTP::spawnMessage,
                        Duration.ofSeconds(3),
                        testKit.scheduler()
                );

        cs.whenComplete((requestActorRef, throwable) -> {
            assertNull(throwable);
            assertEquals("http-master", requestActorRef.path().name());
        });
    }
}