package io.github.ztkmkoo.dss.core.actor;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.testkit.typed.javadsl.ActorTestKit;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:58
 */
public abstract class AbstractDssActorTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDssActorTest.class);

    protected static ActorTestKit testKit;

    @BeforeAll
    static void setUpBeforeClass() {
        testKit = ActorTestKit.create();
    }

    @AfterAll
    static void cleanup() {
        logger.info("AbstractDssActorTest cleanup.");
        testKit.shutdownTestKit();
    }

    protected AbstractDssActorTest() {}
}
