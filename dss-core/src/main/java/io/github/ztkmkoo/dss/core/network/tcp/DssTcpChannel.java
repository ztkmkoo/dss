package io.github.ztkmkoo.dss.core.network.tcp;

import java.util.*;

import io.github.ztkmkoo.dss.core.network.*;
import io.github.ztkmkoo.dss.core.network.tcp.handler.*;
import io.github.ztkmkoo.dss.core.network.tcp.property.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.logging.*;

public class DssTcpChannel implements DssChannel<DssTcpChannelProperty, DssTcpChannelInitializer> {

    @Override
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
