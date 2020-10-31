package io.github.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.actor.property.*;
import io.github.ztkmkoo.dss.core.message.*;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    void initializeMasterActor() {
        final ActorRef<DssMasterCommand> masterActor = newMasterActorRef();
        final TestProbe<DssMasterCommand> probe = testKit.createTestProbe();
        masterActor.tell(new InitializeRequest(probe.getRef()));

        final InitializeResponse response = getProbeResponse(probe, InitializeResponse.class);
        assertNotNull(response);
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
            return masterReceiveBuilder(this)
                    .onMessage(InitializeRequest.class, msg -> {
                        initializeMasterActor(this);
                        Objects.requireNonNull(msg.getSender());
                        msg.getSender().tell(new InitializeResponse());
                        return this;
                    })
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
        public void setResolverActor(ActorRef<DssResolverCommand> actorRef) {

        }

        @Override
        public ActorRef<DssNetworkCommand> getNetworkActor() {
            return null;
        }

        @Override
        public void setNetworkActor(ActorRef<DssNetworkCommand> actorRef) {

        }

        @Override
        public DssMasterActorProperty getDssMasterActorProperty() {
            return new DssMasterActorProperty();
        }

        @Override
        public DssMasterActorStatus getMasterActorStatus() {
            return status;
        }

        @Override
        public void setMasterActorStatus(DssMasterActorStatus status) {
            this.status = status;
        }

        @Override
        public DssBehaviorCreator<DssExceptionCommand, DssExceptionActorProperty> getExceptionBehaviorCreator() {
            return property -> Behaviors.setup(TestActor::new);
        }

        @Override
        public <M extends DssMasterActorProperty> DssExceptionActorProperty createDssExceptionActorProperty(M masterProperty) {
            return new DssExceptionActorProperty() {};
        }

        @Override
        public ActorRef<DssExceptionCommand> getExceptionActor() {
            return null;
        }

        @Override
        public void setExceptionActor(ActorRef<DssExceptionCommand> actorRef) {

        }

        @Override
        public DssBehaviorCreator<DssNetworkCommand, DssNetworkActorProperty> getNetworkBehaviorCreator() {
            return property -> Behaviors.setup(TestActor::new);
        }

        @Override
        public <M extends DssMasterActorProperty> DssNetworkActorProperty createDssNetworkActorProperty(M masterProperty) {
            return new DssNetworkActorProperty() {
                @Override
                public int getBossThreadCount() {
                    return 1;
                }

                @Override
                public int getWorkerThreadCount() {
                    return 1;
                }
            };
        }

        @Override
        public DssBehaviorCreator<DssResolverCommand, DssResolverActorProperty> getResolverBehaviorCreator() {
            return property -> Behaviors.setup(TestActor::new);
        }

        @Override
        public <M extends DssMasterActorProperty> DssResolverActorProperty createDssResolverActorProperty(M masterProperty) {
            return new DssResolverActorProperty() {};
        }

        @Override
        public DssBehaviorCreator<DssServiceCommand, DssServiceActorProperty> getServiceBehaviorCreator() {
            return property -> Behaviors.setup(TestActor::new);
        }

        @Override
        public <M extends DssMasterActorProperty> List<DssServiceActorProperty> createDssServiceActorPropertyList(M masterProperty) {
            return new ArrayList<>();
        }

        @Override
        public <P extends DssServiceActorProperty> DssServiceActorResolvable<String> createDssServiceActorResolvable(P property, ActorRef<DssServiceCommand> actor) {
            return null;
        }

        @Override
        public Map<String, DssServiceActorResolvable<String>> getServiceActorMap() {
            return null;
        }

        @Override
        public void putServiceActorResolvable(String key, DssServiceActorResolvable<String> value) {

        }
    }

    private static class TestActor<T extends DssCommand> extends AbstractBehavior<T> {

        public TestActor(ActorContext<T> context) {
            super(context);
        }

        @Override
        public Receive<T> createReceive() {
            return null;
        }
    }

    private static class InitializeRequest implements DssMasterCommand {
        private static final long serialVersionUID = -6715113886545519228L;

        @Getter
        private final ActorRef<DssMasterCommand> sender;

        @Builder
        public InitializeRequest(ActorRef<DssMasterCommand> sender) {
            this.sender = sender;
        }
    }

    private static class InitializeResponse implements DssMasterCommand {
        private static final long serialVersionUID = -4705847872255308681L;

        @Builder
        public InitializeResponse() { }
    }
}