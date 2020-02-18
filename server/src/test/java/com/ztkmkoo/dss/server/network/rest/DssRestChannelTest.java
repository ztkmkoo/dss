package com.ztkmkoo.dss.server.network.rest;

import com.ztkmkoo.dss.server.network.core.handler.DssChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 12:22
 */
public class DssRestChannelTest {

    @Mock
    private DssChannelInitializer<SocketChannel> dssChannelInitializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void bind() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final DssRestChannel dssRestChannel = new DssRestChannel();

            final ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workerGroup);

            final DssRestChannelProperty property = DssRestChannelProperty
                    .builder(dssChannelInitializer)
                    .port(8181)
                    .build();

            final Channel channel = dssRestChannel.bind(b, property);
            channel.closeFuture();

            assertTrue(serverPortIsOpen(8181));
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private boolean serverPortIsOpen(int port) throws IOException {

        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", port));
        return socket.isConnected();
    }
}