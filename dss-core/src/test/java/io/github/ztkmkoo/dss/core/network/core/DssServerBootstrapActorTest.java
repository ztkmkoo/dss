package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorSystem;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @createYmdt 2020-07-16 03:30
 */
public class DssServerBootstrapActorTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        final ActorSystem<InternalNettyCommand> system = ActorSystem.create(DssServerBootstrapActor.create(), "TEST");
        assertNotNull(system);
        system.tell(InternalNettyCommand
                .InitServerBootstrap
                .builder()
                .bossGroupThread(1)
                .workerGroupThread(16)
                .build()
        );

        system
                .getWhenTerminated()
                .toCompletableFuture()
                .get();
    }
}