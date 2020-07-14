package io.github.ztkmkoo.dss.core.network.rest;

import io.github.ztkmkoo.dss.core.network.rest.handler.DssRestChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 1:04
 */
public class DssRestChannelTest {

    @Test
    public void bind() throws Exception {

        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder()
                .host("127.0.0.1")
                .port(8080)
                .build();

        final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(Collections.emptyList());

        try {
            final DssRestChannel dssRestChannel = new DssRestChannel();
            final Channel channel = dssRestChannel.bind(bootstrap, property, channelInitializer);

            Thread.sleep(1000);

//            final Socket socket = new Socket();
//            socket.connect(new InetSocketAddress("127.0.0.1", 8080));
//
//            assertTrue(socket.isConnected());

//            channel.close().sync();
            channel.closeFuture().sync();
        } finally {
            assertNotNull(workerGroup);
            workerGroup.shutdownGracefully();

            assertNotNull(bossGroup);
            bossGroup.shutdownGracefully();
        }
    }
}