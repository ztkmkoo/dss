package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.github.ztkmkoo.dss.core.network.DssChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DssWebSocketChannelInitializer extends DssChannelInitializer<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(DssWebSocketChannelInitializer.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new WebSocketServerProtocolHandler("/websocket", null, false));
        p.addLast(new DssWebSocketFrameHandler());
    }
}
