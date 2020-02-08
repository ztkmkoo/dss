package com.ztkmkoo.dss.network.http;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 1:17
 */
public class DssHttpServerInitializerTest {

    @Test
    public void initChannel() throws Exception {

        final DssHttpServerInitializer dssHttpServerInitializer = new DssHttpServerInitializer(false, null, DssHttpSimpleHandler::new);

        final SocketChannel socketChannel = new NioSocketChannel();

        dssHttpServerInitializer.initChannel(socketChannel);

        final ChannelPipeline p = socketChannel.pipeline();
        assertEquals(4, p.names().size());  // 3 + 1(default context handler)
        assertEquals(DssHttpSimpleHandler.class, p.last().getClass());
    }
}