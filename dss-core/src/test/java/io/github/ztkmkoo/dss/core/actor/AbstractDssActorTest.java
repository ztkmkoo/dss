package io.github.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:58
 */
@SuppressWarnings("java:S5786")
public class AbstractDssActorTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDssActorTest.class);

    protected static ActorTestKit testKit;

    protected AbstractDssActorTest() {}

    @BeforeAll
    static void setUpBeforeClass() {
        testKit = ActorTestKit.create();
    }

    @AfterAll
    static void cleanup() {
        logger.info("AbstractDssActorTest cleanup.");
        testKit.shutdownTestKit();
    }

    @Test
    void initialize() {
        final TestProbe<TestCommand> probe = testKit.createTestProbe();
        final ActorRef<TestCommand> testActor = testKit.spawn(TestDssActor.create(), "test");
        assertNotNull(testActor);

        final TestCommand req = new TestCommand();
        req.sender = probe.getRef();
        testActor.tell(req);

        final TestCommand res = probe.receiveMessage();
        assertEquals("test", res.name);
    }

    private static class TestDssActor extends AbstractDssActor<TestCommand> {

        private static Behavior<TestCommand> create() {
            return Behaviors.setup(TestDssActor::new);
        }

        private TestDssActor(ActorContext<TestCommand> context) {
            super(context);
        }

        @Override
        public Receive<TestCommand> createReceive() {
            return newReceiveBuilder()
                    .onMessage(TestCommand.class, msg -> {
                        getContext().getLog().info("Test {}", msg);

                        final ActorRef<TestCommand> sender = msg.sender;

                        msg.sender = getSelf();
                        msg.name = getSelf().path().name();

                        sender.tell(msg);

                        return Behaviors.same();
                    })
                    .build();
        }
    }

    private static class TestCommand implements DssCommand {
        private static final long serialVersionUID = -115346833762331684L;

        private ActorRef<TestCommand> sender;
        private String name;
    }
}
