package com.ztkmkoo.dss.server.network.http;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.HttpMessages;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
@Deprecated
class DssHttpSimpleHandler extends AbstractDssHttpHandler {

    private final ActorRef<HttpMessages.Request> masterActor;

    DssHttpSimpleHandler(ActorRef<HttpMessages.Request> masterActor) {
        this.masterActor = masterActor;
    }

    @Override
    protected void handlingHttpRequest(ChannelHandlerContext ctx, HttpRequest request, String content) {
        masterActor.tell(new HttpMessages.Request(ctx, request.uri(), content));
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
