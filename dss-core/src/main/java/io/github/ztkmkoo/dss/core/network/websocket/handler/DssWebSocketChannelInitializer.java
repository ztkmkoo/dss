package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DssWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(DssWebSocketChannelInitializer.class);

    private static final String WEBSOCKET_PATH = "/websocket";
    private final ChannelGroup group;

    public DssWebSocketChannelInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        p.addLast(new DssWebSocketHandler());
        p.addLast(new DssTextWebSocketFrameHandler(group));
    }
}
