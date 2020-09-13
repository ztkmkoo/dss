package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class DssHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Logger logger = LoggerFactory.getLogger(DssHttpRequestHandler.class);

    private final String WEBSOCKET_PATH;
    private WebSocketServerHandshaker handshaker;
    private FullHttpRequest request;

    public DssHttpRequestHandler(String WEBSOCKET_PATH) {
        this.WEBSOCKET_PATH = WEBSOCKET_PATH;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (msg != null) {
            request = msg;
            handleHttpRequest(ctx, request);
        } else {
            throw new UnsupportedOperationException("FullHttpRequest message is null");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws InterruptedException {
        final String uri = req.uri();
        logger.info("Request URI: " + uri);

        if (!req.decoderResult().isSuccess()) {
            logger.info("BAD REQUEST");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST, ctx.alloc().buffer(0)));
            return;
        }

        if (!GET.equals(req.method())) {
            logger.info("GET method is required");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN, ctx.alloc().buffer(0)));
            return;
        }

        int baseIndex = uri.indexOf("?");
        boolean checkEndPoint = baseIndex == -1 ? WEBSOCKET_PATH.equals(uri) : WEBSOCKET_PATH.equals(uri.substring(0, baseIndex));

        if (checkEndPoint) {
            logger.info("Request WebSocket Protocol");

            HttpHeaders headers = req.headers();

            if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION)) &&
                    "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
                handleHandshake(ctx, req);
                ctx.fireChannelRead(req.retain());
            } else {
                throw new InterruptedException("Websocket header field is needed");
            }

        } else if ("/".equals(req.uri())) {

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
        logger.info("Handshake Start");

        String websocketURL = getWebSocketLocation(ctx.pipeline(), req, WEBSOCKET_PATH);
        logger.info(websocketURL);

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(websocketURL, null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req).sync();
            logger.info("Handshake Success");
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("ExceptionCaught", cause);

        ctx.close();
    }
}
