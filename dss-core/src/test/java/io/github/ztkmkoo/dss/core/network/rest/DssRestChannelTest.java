package io.github.ztkmkoo.dss.core.network.rest;

import io.github.ztkmkoo.dss.core.network.rest.handler.DssRestChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 1:04
 */
public class DssRestChannelTest {

    @Test
    public void bind() throws Exception {

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder()
                .host("127.0.0.1")
                .port(8181)
                .build();

        final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(Collections.emptyList());

        try {
            final DssRestChannel dssRestChannel = new DssRestChannel();
            final Channel channel = dssRestChannel.bind(bootstrap, property, channelInitializer);

            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 8181));

            assertTrue(socket.isConnected());

            channel.close().sync();
        } finally {
            assertNotNull(workerGroup);
            workerGroup.shutdownGracefully();

            assertNotNull(bossGroup);
            bossGroup.shutdownGracefully();
        }
    }
}