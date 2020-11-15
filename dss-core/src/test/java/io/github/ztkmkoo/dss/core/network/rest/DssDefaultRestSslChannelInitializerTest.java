package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelInitializerProperty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-15 22:37
 */
class DssDefaultRestSslChannelInitializerTest extends AbstractDssActorTest {

    @Test
    void constructor() throws Exception {
        final TestProbe<DssResolverCommand> probe = testKit.createTestProbe(DssResolverCommand.class);
        final DssRestChannelInitializerProperty property = new DssRestChannelInitializerProperty(true);
        final ChannelInitializer<SocketChannel> channelInitializer = DssRestChannelInitializer.createChannelInitializer(probe.getRef(), property);

        assertNotNull(channelInitializer);
        if (channelInitializer instanceof DssDefaultRestSslChannelInitializer) {
            final Field field = DssDefaultRestSslChannelInitializer.class.getDeclaredField("sslCtx");
            field.setAccessible(true);
            final Object object = field.get(channelInitializer);

            if (object instanceof SslContext) {
                final SslContext context = (SslContext) object;
                assertNotNull(context);
                assertTrue(context.isServer());
            } else {
                fail();
            }
        } else {
            fail();
        }
    }
}