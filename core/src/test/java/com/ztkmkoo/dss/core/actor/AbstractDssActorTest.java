package com.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import org.junit.AfterClass;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:58
 */
public abstract class AbstractDssActorTest {

    protected static final ActorTestKit testKit = ActorTestKit.create();

    @AfterClass
    public static void cleanup() {
        testKit.shutdownTestKit();
    }
}
