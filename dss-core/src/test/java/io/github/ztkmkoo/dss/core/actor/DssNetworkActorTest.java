package io.github.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-13 04:42
 */
@Slf4j
class DssNetworkActorTest extends AbstractDssActorTest {

    @Test
    void bind() throws IOException, InterruptedException {
        final String host = "0.0.0.0";
        final int port = 8080;

        final TestProbe<DssMasterCommand> probe = testKit.createTestProbe(DssMasterCommand.class);
        final ActorRef<DssNetworkCommand> networkActor = testKit.spawn(TestNetworkActor.create(probe.getRef()));
        networkActor.tell(DssNetworkCommand.Bind.builder().host(host).port(port).logLevel(DssLogLevel.INFO).build());

        final DssMasterCommand msg = probe.receiveMessage();
        if (msg instanceof DssMasterCommand.StatusUpdate) {
            final DssMasterCommand.StatusUpdate update = (DssMasterCommand.StatusUpdate) msg;
            assertEquals(DssMasterActorStatus.START, update.getStatus());
        } else {
            fail();
        }

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));

        assertTrue(socket.isConnected());
        socket.close();

        networkActor.tell(DssNetworkCommand.Close.INST);

        final DssMasterCommand msg2 = probe.receiveMessage();
        if (msg2 instanceof DssMasterCommand.StatusUpdate) {
            final DssMasterCommand.StatusUpdate update = (DssMasterCommand.StatusUpdate) msg2;
            assertEquals(DssMasterActorStatus.SHUTDOWN, update.getStatus());
        } else {
            fail();
        }

        // wait server down
        final CountDownLatch latch = new CountDownLatch(1);
        latch.await(1, TimeUnit.SECONDS);

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            assertFalse(socket.isConnected());
        } catch (ConnectException e) {
            log.debug("Expected not connect exception", e);
            assertNotNull(e);
        }
    }

    private static class TestNetworkActor extends AbstractDssActor<DssNetworkCommand> implements DssNetworkActor<DssRestChannelProperty, SocketChannel> {

        private static Behavior<DssNetworkCommand> create(ActorRef<DssMasterCommand> masterActor) {
            return Behaviors.setup(context -> new TestNetworkActor(context, masterActor));
        }

        @Getter @Setter
        private EventLoopGroup bossGroup = new NioEventLoopGroup();

        @Getter @Setter
        private EventLoopGroup workerGroup = new NioEventLoopGroup();

        @Getter @Setter
        private ActorRef<DssMasterCommand> masterActor;

        @Setter
        private boolean active;

        private TestNetworkActor(ActorContext<DssNetworkCommand> context, ActorRef<DssMasterCommand> masterActor) {
            super(context);
            this.masterActor = masterActor;
        }

        @Override
        public Receive<DssNetworkCommand> createReceive() {
            return networkReceiveBuilder(this)
                    .build();
        }

        @Override
        public DssRestChannelProperty convertToChannelProperty(DssNetworkCommand.Bind msg) {
            return DssRestChannelProperty
                    .builder()
                    .host(msg.getHost())
                    .port(msg.getPort())
                    .dssLogLevel(msg.getLogLevel())
                    .build();
        }

        @Override
        public ChannelInitializer<SocketChannel> getChannelInitializer() {
            return new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                }
            };
        }

        @Override
        public boolean getActive() {
            return active;
        }

        @Override
        public ActorRef<DssResolverCommand> getResolverActor() {
            return null;
        }

        @Override
        public void setResolverActor(ActorRef<DssResolverCommand> actorRef) {

        }
    }
}