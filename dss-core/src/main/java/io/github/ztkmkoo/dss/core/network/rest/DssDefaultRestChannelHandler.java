package io.github.ztkmkoo.dss.core.network.rest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-22 07:24
 */
public class DssDefaultRestChannelHandler extends SimpleChannelInboundHandler<Object> implements DssRestChannelHandler {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            buffer.setLength(0);
        }

        if (msg instanceof HttpContent) {
            final HttpContent httpContent = (HttpContent) msg;
            final ByteBuf content = httpContent.content();

            if (content.isReadable()) {
                buffer.append(content.toString(CharsetUtil.UTF_8));
            }

            if (msg instanceof LastHttpContent) {
                ctx.writeAndFlush(new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(buffer.toString(), CharsetUtil.UTF_8))
                );
            }
        }
    }
}
