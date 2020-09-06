package io.github.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.actor.property.DssMasterActorProperty;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-30 01:45
 */
class DssMasterActorTest extends AbstractDssActorTest {

    private static ActorRef<DssMasterCommand> newMasterActorRef() {
        return testKit.spawn(TestMasterActor.create());
    }

    private static  <T extends DssCommand, R extends T> R getProbeResponse(TestProbe<T> probe, Class<R> rClass) {
        return getProbeResponse(probe, rClass, Duration.ofSeconds(1));
    }

    @SuppressWarnings("unchecked")
    private static  <T extends DssCommand, R extends T> R getProbeResponse(TestProbe<T> probe, Class<R> rClass, Duration duration) {
        final T msg = probe.receiveMessage(duration);
        Objects.requireNonNull(msg);

        if (msg.getClass().equals(rClass)) {
            return (R)msg;
        } else {
            fail();
            // won't call here
            return null;
        }
    }

    @Test
    void spawnResolverActor() {
        final ActorRef<DssMasterCommand> masterActor = newMasterActorRef();
        final TestProbe<DssMasterCommand> probe = testKit.createTestProbe();
        masterActor.tell(ResolverActorRefRequest.builder().sender(probe.getRef()).build());

        final ResolverActorRefResponse response = getProbeResponse(probe, ResolverActorRefResponse.class);
        assertNotNull(response.getActorRef());
    }

    @Test
    void spawnNetworkActor() {
        final ActorRef<DssMasterCommand> masterActor = newMasterActorRef();
        final TestProbe<DssMasterCommand> probe = testKit.createTestProbe();
        masterActor.tell(NetworkActorRefRequest.builder().sender(probe.getRef()).build());

        final NetworkActorRefResponse response = getProbeResponse(probe, NetworkActorRefResponse.class);
        assertNotNull(response.getActorRef());
    }

    @Test
    void masterActorStatus() throws InterruptedException {
        final ActorRef<DssMasterCommand> masterActor = newMasterActorRef();
        final TestProbe<DssCommand> probe = testKit.createTestProbe();

        // check initial status
        masterActor.tell(DssMasterCommand.StatusRequest.builder().sender(probe.getRef()).build());
        final DssMasterCommand.StatusResponse response = getProbeResponse(probe, DssMasterCommand.StatusResponse.class);
        assertEquals(DssMasterActorStatus.READY, response.getStatus());

        // update status and wait 1 sec
        masterActor.tell(DssMasterCommand.StatusUpdate.builder().status(DssMasterActorStatus.START).build());
        final CountDownLatch latch = new CountDownLatch(1);
        latch.await(1, TimeUnit.SECONDS);

        // check after update
        masterActor.tell(DssMasterCommand.StatusRequest.builder().sender(probe.getRef()).build());
        final DssMasterCommand.StatusResponse response2 = getProbeResponse(probe, DssMasterCommand.StatusResponse.class);
        assertEquals(DssMasterActorStatus.START, response2.getStatus());
    }

    private static class TestMasterActor extends AbstractDssActor<DssMasterCommand> implements DssMasterActor {

        private static Behavior<DssMasterCommand> create() {
            return Behaviors.setup(TestMasterActor::new);
        }

        private DssMasterActorStatus status = DssMasterActorStatus.READY;

        private TestMasterActor(ActorContext<DssMasterCommand> context) {
            super(context);
        }

        @Override
        public Receive<DssMasterCommand> createReceive() {
            return newReceiveBuilder()
                    .onMessage(ResolverActorRefRequest.class, msg -> {
                        getLog().info("Receive: {}", msg);
                        final ActorRef<DssResolverCommand> resolver = spawnResolverActor(this);
                        msg.getSender().tell(ResolverActorRefResponse.builder().actorRef(resolver).build());
                        return Behaviors.same();
                    })
                    .onMessage(NetworkActorRefRequest.class, msg -> {
                        getLog().info("Receive: {}", msg);
                        final ActorRef<DssNetworkCommand> network = spawnNetworkActor(this);
                        msg.getSender().tell(NetworkActorRefResponse.builder().actorRef(network).build());
                        return Behaviors.same();
                    })
                    .onMessage(DssMasterCommand.StatusRequest.class, this::handlingStatusRequest)
                    .onMessage(DssMasterCommand.StatusUpdate.class, this::handlingStatusUpdate)
                    .build();
        }

        @Override
        public Logger getLog() {
            return getContext().getLog();
        }

        @Override
        public ActorRef<DssResolverCommand> getResolverActor() {
            return null;
        }

        @Override
        public ActorRef<DssNetworkCommand> getNetworkActor() {
            return null;
        }

        @Override
        public DssMasterActorProperty getProperty() {
            return null;
        }

        @Override
        public Behavior<DssResolverCommand> createResolverActorBehavior() {
            return Behaviors
                    .receive(DssResolverCommand.class)
                    .build();
        }

        @Override
        public Behavior<DssNetworkCommand> createNetworkActorBehavior() {
            return Behaviors
                    .receive(DssNetworkCommand.class)
                    .build();
        }

        @Override
        public DssMasterActorStatus getMasterActorStatus() {
            return status;
        }

        @Override
        public void setMasterActorStatus(DssMasterActorStatus status) {
            this.status = status;
        }
    }

    private static class ResolverActorRefRequest implements DssMasterCommand{
        private static final long serialVersionUID = -6715113886545519228L;

        @Getter
        private final ActorRef<DssMasterCommand> sender;

        @Builder
        public ResolverActorRefRequest(ActorRef<DssMasterCommand> sender) {
            this.sender = sender;
        }
    }

    private static class ResolverActorRefResponse implements DssMasterCommand {
        private static final long serialVersionUID = 6219916186960923214L;

        @Getter
        private final ActorRef<DssResolverCommand> actorRef;

        @Builder
        public ResolverActorRefResponse(ActorRef<DssResolverCommand> actorRef) {
            this.actorRef = actorRef;
        }
    }

    private static class NetworkActorRefRequest implements DssMasterCommand {
        private static final long serialVersionUID = 249969752402472135L;

        @Getter
        private final ActorRef<DssMasterCommand> sender;

        @Builder
        public NetworkActorRefRequest(ActorRef<DssMasterCommand> sender) {
            this.sender = sender;
        }
    }

    private static class NetworkActorRefResponse implements DssMasterCommand {
        private static final long serialVersionUID = -7299143779560419564L;

        @Getter
        private final ActorRef<DssNetworkCommand> actorRef;

        @Builder
        public NetworkActorRefResponse(ActorRef<DssNetworkCommand> actorRef) {
            this.actorRef = actorRef;
        }
    }
}