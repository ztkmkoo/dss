package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelInitializerProperty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-15 22:26
 */
class DssRestChannelInitializerTest extends AbstractDssActorTest {

    @Test
    void createChannelInitializer() throws InterruptedException {
        final TestProbe<DssResolverCommand> probe = testKit.createTestProbe();
        final DssRestChannelInitializerProperty property = new DssRestChannelInitializerProperty();
        final ChannelInitializer<SocketChannel> channelInitializer = DssRestChannelInitializer.createChannelInitializer(probe.getRef(), property);

        assertNotNull(channelInitializer);
    }
}