package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DssWebSocketHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(DssWebSocketHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof WebSocketFrame) {
            logger.info("Client Channel : " + ctx.channel());

            if (msg instanceof BinaryWebSocketFrame) {
                logger.info("BinaryWebSocketFrame Received");

            } else if (msg instanceof TextWebSocketFrame) {
                logger.info("TextWebSocketFrame Received");

                final TextWebSocketFrame textWebSocketFrame =  (TextWebSocketFrame) msg;
                ctx.channel().writeAndFlush(textWebSocketFrame.text());

                logger.info("Received Message : " + textWebSocketFrame.text());
            } else if (msg instanceof PingWebSocketFrame) {
                logger.info("PingWebSocketFrame Received");

            } else if (msg instanceof PongWebSocketFrame) {
                logger.info("PongWebSocketFrame Received");

            } else if (msg instanceof CloseWebSocketFrame) {
                logger.info("CloseWebSocketFrame Received");

                final CloseWebSocketFrame closeWebSocketFrame = (CloseWebSocketFrame) msg;
                logger.info("ReasonText : " + closeWebSocketFrame.reasonText());
                logger.info("StatusCode : " + closeWebSocketFrame.statusCode());
            } else {
                throw new UnsupportedOperationException("Unsupported WebSocketFrame Type " + msg.getClass().getName());
            }
        }
    }
}

