package com.ztkmkoo.dss.server.actor.core;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.typed.ActorRef;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.AskPattern;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.message.ServerMessages;
import org.junit.AfterClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 1:08
 */
public class DssServerMasterActorTest {

    private static final ActorTestKit testKit = ActorTestKit.create();

    @AfterClass
    public static void cleanup() {
        testKit.system().log().info("cleanup");
        testKit.shutdownTestKit();
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

    @Test(expected = InstantiationException.class)
    public void constructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Constructor<DssServerMasterActor> constructor = DssServerMasterActor.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}