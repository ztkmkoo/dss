package com.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:58
 */
public abstract class AbstractDssActorTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDssActorTest.class);

    protected static ActorTestKit testKit;

    @BeforeClass
    public static void setUpBeforeClass() {
        testKit = ActorTestKit.create();
    }

    @AfterClass
    public static void cleanup() {
        logger.info("AbstractDssActorTest cleanup.");
        testKit.shutdownTestKit();
    }
}
