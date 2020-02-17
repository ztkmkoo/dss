package com.ztkmkoo.dss.server.network.core;

import com.ztkmkoo.dss.server.network.http.DssHttpServerProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public class AbstractDssServerTest {

    private final DssServerProperty property = DssHttpServerProperty
            .builder(false)
            .port(8001)
            .build();

    @Test
    public void bind() throws InterruptedException {

        final DssServer dssServer = property.getNetworkType().getCreator().create();
        assertNotNull(dssServer);

        final EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {
            final ServerBootstrap b = new ServerBootstrap().group(boosGroup, workerGroup);
            final Channel channel = dssServer.bind(b, property, null);

            Objects.requireNonNull(channel);

            channel.close().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}