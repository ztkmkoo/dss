package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DssWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        log.info("channelRead0: {}", webSocketFrame);

        if (webSocketFrame instanceof TextWebSocketFrame) {
            final String request = ((TextWebSocketFrame) webSocketFrame).text();
            log.info("request: {}", request);
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame("Test"));
        } else {
            throw new UnsupportedOperationException("unsupported frame type: " + webSocketFrame.getClass().getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error: ", cause);
        ctx.close();
    }
}
