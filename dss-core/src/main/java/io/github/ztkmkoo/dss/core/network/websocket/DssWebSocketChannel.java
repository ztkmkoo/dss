package io.github.ztkmkoo.dss.core.network.websocket;

import io.github.ztkmkoo.dss.core.network.DssChannel;
import io.github.ztkmkoo.dss.core.network.websocket.handler.DssWebSocketChannelInitializer;
import io.github.ztkmkoo.dss.core.network.websocket.property.DssWebSocketChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Objects;

public class DssWebSocketChannel implements DssChannel<DssWebSocketChannelProperty, DssWebSocketChannelInitializer> {

    @Override
    public Channel bind(ServerBootstrap serverBootstrap, DssWebSocketChannelProperty dssWebsocketChannelProperty,
                        DssWebSocketChannelInitializer dssWebsocketChannelInitializer) throws InterruptedException {

        Objects.requireNonNull(serverBootstrap);
        Objects.requireNonNull(dssWebsocketChannelProperty);
        Objects.requireNonNull(dssWebsocketChannelInitializer);

        return serverBootstrap
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(dssWebsocketChannelProperty.getDssLogLevel().getNettyLogLevel()))
                .childHandler(dssWebsocketChannelInitializer)
                .bind(dssWebsocketChannelProperty.getHost(), dssWebsocketChannelProperty.getPort())
                .sync()
                .channel();
    }
}
