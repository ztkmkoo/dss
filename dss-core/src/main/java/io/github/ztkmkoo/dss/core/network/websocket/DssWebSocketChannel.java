package io.github.ztkmkoo.dss.core.network.websocket;

import io.github.ztkmkoo.dss.core.network.DssChannel;
import io.github.ztkmkoo.dss.core.network.websocket.handler.DssWebSocketChannelInitializer;
import io.github.ztkmkoo.dss.core.network.websocket.property.DssWebSocketChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class DssWebSocketChannel implements DssChannel<DssWebSocketChannelProperty, SocketChannel, DssWebSocketChannelInitializer> {

    @Override
    public Channel bind(ServerBootstrap serverBootstrap, DssWebSocketChannelProperty dssChannelProperty, DssWebSocketChannelInitializer dssChannelInitializer) throws InterruptedException {
        validate(serverBootstrap, dssChannelProperty, dssChannelInitializer);

        return serverBootstrap
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(dssChannelInitializer)
                .bind(dssChannelProperty.getHost(), dssChannelProperty.getPort())
                .sync()
                .channel();
    }
}
