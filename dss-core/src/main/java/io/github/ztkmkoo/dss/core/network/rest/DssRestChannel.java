package io.github.ztkmkoo.dss.core.network.rest;

import io.github.ztkmkoo.dss.core.network.rest.handler.DssRestChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:43
 */
public class DssRestChannel {

    public Channel bind(ServerBootstrap serverBootstrap, DssRestChannelProperty dssRestChannelProperty, DssRestChannelInitializer dssRestChannelInitializer) throws InterruptedException {

        Objects.requireNonNull(serverBootstrap);
        Objects.requireNonNull(dssRestChannelProperty);
        Objects.requireNonNull(dssRestChannelInitializer);

        return serverBootstrap
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(dssRestChannelInitializer)
                .bind(dssRestChannelProperty.getHost(), dssRestChannelProperty.getPort())
                .sync()
                .channel();
    }
}
