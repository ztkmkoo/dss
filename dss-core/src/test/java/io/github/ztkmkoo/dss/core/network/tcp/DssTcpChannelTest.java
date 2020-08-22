package io.github.ztkmkoo.dss.core.network.tcp;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import io.github.ztkmkoo.dss.core.network.tcp.handler.DssTcpChannelInitializer;
import io.github.ztkmkoo.dss.core.network.tcp.property.DssTcpChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

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