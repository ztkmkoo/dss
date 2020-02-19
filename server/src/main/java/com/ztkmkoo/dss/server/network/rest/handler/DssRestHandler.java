package com.ztkmkoo.dss.server.network.rest.handler;

import com.ztkmkoo.dss.server.network.rest.entity.DssRestErrorResponses;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestRequest;
import com.ztkmkoo.dss.server.network.rest.enumeration.DssRestMethod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 1:21
 */
public class DssRestHandler extends SimpleChannelInboundHandler<Object> {

    protected final Logger logger = LoggerFactory.getLogger(DssRestHandler.class);
    private final StringBuilder buffer = new StringBuilder();

    private HttpRequest request;

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

        final DssRestRequest dssRestRequest = dssRestRequest(request, buffer.toString());
        logger.info("channelReadComplete: {}", dssRestRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        logger.error("exceptionCaught", cause);
        if (cause instanceof ReadTimeoutException) {
            ctx
                    .writeAndFlush(DssRestErrorResponses.REQUEST_TIMEOUT)
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.close();
        }
    }

    private static DssRestRequest dssRestRequest(HttpRequest request, String content) {

        Objects.requireNonNull(request);
        Objects.requireNonNull(content);

        return DssRestRequest
                .builder()
                .uri(request.uri())
                .method(DssRestMethod.fromNettyHttpMethod(request.method()))
                .content(content)
                .build();
    }
}
