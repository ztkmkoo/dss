package io.github.ztkmkoo.dss.core.network.tcp;

import io.github.ztkmkoo.dss.core.network.tcp.handler.DssTcpChannelInitializer;
import io.github.ztkmkoo.dss.core.network.tcp.property.DssTcpChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Objects;

public class DssTcpChannel {

    public Channel bind(ServerBootstrap serverBootstrap, DssTcpChannelProperty dssTcpChannelProperty,
        DssTcpChannelInitializer dssTcpChannelInitializer) throws InterruptedException {

        Objects.requireNonNull(serverBootstrap);
        Objects.requireNonNull(dssTcpChannelProperty);
        Objects.requireNonNull(dssTcpChannelInitializer);

        return serverBootstrap
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.DEBUG))
            .childHandler(dssTcpChannelInitializer)
            .bind(dssTcpChannelProperty.getHost(), dssTcpChannelProperty.getPort())
            .sync()
            .channel();
    }
}
