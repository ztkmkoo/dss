package io.github.ztkmkoo.dss.core.network.websocket;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetSocketAddress;
import java.net.Socket;

import io.github.ztkmkoo.dss.core.network.websocket.handler.DssWebSocketChannelInitializer;
import io.github.ztkmkoo.dss.core.network.websocket.property.DssWebSocketChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.jupiter.api.Test;

class DssWebSocketChannelTest {

    @Test
    void bind() throws Exception {

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssWebSocketChannelProperty property = DssWebSocketChannelProperty
                .builder()
                .host("127.0.0.1")
                .port(8181)
                .build();

        final DssWebSocketChannelInitializer channelInitializer = new DssWebSocketChannelInitializer();

        try {
            final DssWebSocketChannel dssWebSocketChannel = new DssWebSocketChannel();
            final Channel channel = dssWebSocketChannel.bind(bootstrap, property, channelInitializer);

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
