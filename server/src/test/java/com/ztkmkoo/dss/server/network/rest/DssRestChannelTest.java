package com.ztkmkoo.dss.server.network.rest;

import com.ztkmkoo.dss.server.network.rest.handler.DssRestChannelInitializer;
import com.ztkmkoo.dss.server.network.rest.handler.DssRestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 12:22
 */
public class DssRestChannelTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void bind() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bindServer(bossGroup, workerGroup, new DssRestChannelInitializer(), 8181);
            assertTrue(serverPortIsOpen(8181));
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Test(expected = SocketTimeoutException.class)
    public void bindAndSendTimeout() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final DssRestHandler handler = new DssRestHandler();
            final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(handler);
            bindServer(bossGroup, workerGroup, channelInitializer, 8181);
            sendMessageToServer(8181);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void bindServer(
            NioEventLoopGroup bossGroup,
            NioEventLoopGroup workerGroup,
            DssRestChannelInitializer dssRestChannelInitializer,
            int port
    ) throws InterruptedException {

        final DssRestChannel dssRestChannel = new DssRestChannel();

        final ServerBootstrap b = new ServerBootstrap()
                .group(bossGroup, workerGroup);

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder(dssRestChannelInitializer)
                .port(port)
                .build();

        final Channel channel = dssRestChannel.bind(b, property);
        channel.closeFuture();
    }

    private boolean serverPortIsOpen(int port) throws IOException {

        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", port));
        return socket.isConnected();
    }

    private void sendMessageToServer(int port) throws IOException {

        final Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress("127.0.0.1", port));

        if (socket.isConnected()) {
            final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            outputStream.writeByte(1);
            outputStream.flush();

            final String response = reader.readLine();
            System.err.println(response);
        }
    }
}