package com.ztkmkoo.dss.network.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
class DssHttpSimpleHandler extends AbstractDssHttpHandler {

    @Override
    protected void handlingHttpRequest(ChannelHandlerContext ctx, HttpRequest request, String content) {

        writeResponse(
                ctx,
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(content, CharsetUtil.UTF_8)
                )
        );
    }

    @Override
    protected void handlingHttpRequest(ChannelHandlerContext ctx, HttpRequest request) {

        writeResponse(
                ctx,
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.EMPTY_BUFFER
                )
        );
    }

    private void writeResponse(ChannelHandlerContext ctx, FullHttpResponse response) {
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
