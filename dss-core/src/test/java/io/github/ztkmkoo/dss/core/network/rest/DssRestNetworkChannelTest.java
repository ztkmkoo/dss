package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-01 03:21
 */
class DssRestNetworkChannelTest extends AbstractDssActorTest {

    @Test
    void bind() throws IOException, InterruptedException {
        final TestProbe<DssNetworkCommand> networkProbe = testKit.createTestProbe(DssNetworkCommand.class);
        final TestProbe<DssResolverCommand> resolverProbe = testKit.createTestProbe(DssResolverCommand.class);

        final DssRestNetworkChannel channel = new DssRestNetworkChannel();
        channel.bind(
                DssNetworkCommand.Bind
                        .builder()
                        .host("0.0.0.0")
                        .port(8080)
                        .logLevel(DssLogLevel.DEBUG)
                        .build(),
                networkProbe.getRef(),
                resolverProbe.getRef()
        );

        assertNotNull(channel);

        final CountDownLatch latch = new CountDownLatch(1);
        latch.await(1, TimeUnit.SECONDS);

        validateServerBinding("127.0.0.1", 8080);

        channel.close();
    }

    private void validateServerBinding(String host, int port) throws IOException {
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));

        assertTrue(socket.isConnected());
    }
}