package io.github.ztkmkoo.dss.core.network.websocket;

import io.github.ztkmkoo.dss.core.network.websocket.handler.DssWebSocketChannelInitializer;
import io.github.ztkmkoo.dss.core.network.websocket.property.DssWebSocketChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DssWebSocketChannelTest {

    @Test
    public void bindTest() throws InterruptedException, IOException {
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssWebSocketChannelProperty property = DssWebSocketChannelProperty
                .builder()
                .host("localhost")
                .port(8181)
                .build();

        final DssWebSocketChannelInitializer channelInitializer = new DssWebSocketChannelInitializer();

        try {
            final DssWebSocketChannel webSocketChannel = new DssWebSocketChannel();
            final Channel channel = webSocketChannel.bind(bootstrap, property, channelInitializer);

            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 8181));

            assertTrue(socket.isConnected());

            socket.close();

            channel.close().sync();
        } finally {
            assertNotNull(workerGroup);
            workerGroup.shutdownGracefully();

            assertNotNull(bossGroup);
            bossGroup.shutdownGracefully();
        }
    }
}