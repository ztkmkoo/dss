package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

@ChannelHandler.Sharable
public class DssWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private Logger logger = LoggerFactory.getLogger(DssWebSocketHandler.class);

    private static final String WEBSOCKET_PATH = "/websocket";
    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            logger.info("FullHttpRequest");
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            logger.info("WebsocketFrame");
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        } else {
            String message = "unsupported frame tye";
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        logger.info("Request URI: " + req.uri());

        if (!req.decoderResult().isSuccess()) {
            logger.info("BAD REQUEST");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST, ctx.alloc().buffer(0)));
            return;
        }

        if (!GET.equals(req.method())) {
            logger.info("NO GET method");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN, ctx.alloc().buffer(0)));
            return;
        }

        if (WEBSOCKET_PATH.equalsIgnoreCase(req.uri())) {
            logger.info("Request WebSocket Upgrade");
            HttpHeaders headers = req.headers();

            if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION)) &&
                    "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
                logger.info("Start handshake");
                try {
                    handleHandshake(ctx, req);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            ctx.fireUserEventTriggered(req.retain());
        }

        if ("/".equals(req.uri())) {

            ByteBuf content = req.content();
            FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, content);

            res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpUtil.setContentLength(res, content.readableBytes());

            sendHttpResponse(ctx, req, res);

        } else {
            logger.info("NOT FOUND");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), NOT_FOUND, ctx.alloc().buffer(0)));
        }
    }

    private void handleHandshake(ChannelHandlerContext ctx, FullHttpRequest req) throws InterruptedException {
        String websocketURL = getWebSocketLocation(ctx.pipeline(), req, WEBSOCKET_PATH);
        logger.info(websocketURL);

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(websocketURL, null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req).sync();
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (frame instanceof CloseWebSocketFrame) {
            logger.info("Close WebsocketFrame");
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            logger.info("Ping WebsocketFrame");
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {

        HttpResponseStatus responseStatus = res.status();
        if (responseStatus.code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }
        boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
        HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture f = ctx.writeAndFlush(res.retain());
        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline pipeline, HttpRequest request, String path) {
        String protocol = "ws";
        if (pipeline.get(SslHandler.class) != null) {
            protocol = "wss";
        }
        return protocol + "://" + request.headers().get(HttpHeaderNames.HOST) + path;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);

        ctx.close();
    }
}
