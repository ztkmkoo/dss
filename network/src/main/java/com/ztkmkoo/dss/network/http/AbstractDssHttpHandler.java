package com.ztkmkoo.dss.network.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public abstract class AbstractDssHttpHandler extends SimpleChannelInboundHandler<Object> {

    protected final Logger logger = Logger.getLogger(DssHttpSimpleHandler.class.getSimpleName());
    private final StringBuilder buffer = new StringBuilder();

    private HttpRequest request;

    protected abstract void handlingHttpRequest(ChannelHandlerContext ctx, HttpRequest request, String content);
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
        logger.log(Level.WARNING, "exceptionCaught", cause);
        ctx.close();
    }
}
