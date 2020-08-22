package io.github.ztkmkoo.dss.core.network.tcp;

import static org.junit.jupiter.api.Assertions.*;

import java.net.*;

import org.junit.jupiter.api.*;

import io.github.ztkmkoo.dss.core.network.tcp.handler.*;
import io.github.ztkmkoo.dss.core.network.tcp.property.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;

class DssTcpChannelTest {

    @Test
    public void bind() throws Exception {

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssTcpChannelProperty property = DssTcpChannelProperty
            .builder()
            .host("127.0.0.1")
            .port(8181)
            .build();

        final DssTcpChannelInitializer channelInitializer = new DssTcpChannelInitializer();

        try {
            final DssTcpChannel dssTcpChannel = new DssTcpChannel();
            final Channel channel = dssTcpChannel.bind(bootstrap, property, channelInitializer);

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