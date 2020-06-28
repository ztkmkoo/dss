package io.github.ztkmkoo.dss.core.network.rest;

import io.github.ztkmkoo.dss.core.network.DssChannel;
import io.github.ztkmkoo.dss.core.network.rest.handler.DssRestChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:43
 */
public class DssRestChannel implements DssChannel<DssRestChannelProperty, SocketChannel, DssRestChannelInitializer> {

    @Override
    public Channel bind(ServerBootstrap serverBootstrap, DssRestChannelProperty dssChannelProperty, DssRestChannelInitializer dssRestChannelInitializer) throws InterruptedException {
        validate(serverBootstrap, dssChannelProperty, dssRestChannelInitializer);

        return serverBootstrap
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(dssRestChannelInitializer)
                .bind(dssChannelProperty.getHost(), dssChannelProperty.getPort())
                .sync()
                .channel();
    }
}
