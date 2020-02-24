package com.ztkmkoo.dss.server.network.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
@Deprecated
public abstract class AbstractDssHttpHandler extends SimpleChannelInboundHandler<Object> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractDssHttpHandler.class);
    private final StringBuilder buffer = new StringBuilder();

    private HttpRequest request;

    /**
     * handling http request
     * @param ctx: ChannelHandlerContext, for sending message to client
     * @param request: HttpRequest, HttpRequestDecoder deserialize it
     * @param content: HttpContent, UTF-8 encoding
     */
    protected abstract void handlingHttpRequest(ChannelHandlerContext ctx, HttpRequest request, String content);

    /**
     * handling http request
     * @param ctx: ChannelHandlerContext, for sending message to client
     * @param request: HttpRequest, HttpRequestDecoder deserialize it
     */
    protected abstract void handlingHttpRequest(ChannelHandlerContext ctx, HttpRequest request);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof HttpRequest) {

            request = (HttpRequest) msg;
            buffer.setLength(0);
        }

        if (msg instanceof HttpContent) {

            final HttpContent httpContent = (HttpContent) msg;

            final ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                buffer.append(content.toString(CharsetUtil.UTF_8));
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        Objects.requireNonNull(request);

        if (buffer.length() > 0) {
            handlingHttpRequest(ctx, request, buffer.toString());
        } else {
            handlingHttpRequest(ctx, request);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("exceptionCaught", cause);
        ctx.close();
    }
}
