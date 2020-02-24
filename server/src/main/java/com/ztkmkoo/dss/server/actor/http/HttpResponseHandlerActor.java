package com.ztkmkoo.dss.server.actor.http;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.ztkmkoo.dss.server.message.HttpMessages;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 13. 오후 9:43
 */
@Deprecated
public class HttpResponseHandlerActor extends AbstractBehavior<HttpMessages.Response> {

    public static Behavior<HttpMessages.Response> create() {
        return Behaviors.setup(HttpResponseHandlerActor::new);
    }

    private HttpResponseHandlerActor(ActorContext<HttpMessages.Response> context) {
        super(context);
    }

    @Override
    public Receive<HttpMessages.Response> createReceive() {
        return newReceiveBuilder().onMessage(HttpMessages.Response.class, this::onResponse).build();
    }

    private Behavior<HttpMessages.Response> onResponse(HttpMessages.Response response) {
        getContext().getLog().info("on HttpMessages.Request: {}", response);

        if (Objects.nonNull(response.getCtx())) {
            response
                    .getCtx()
                    .writeAndFlush(new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            Unpooled.copiedBuffer(response.getContent(), CharsetUtil.UTF_8)
                    )).addListener(ChannelFutureListener.CLOSE);
        } else {
            getContext().getLog().debug("ctx is null");
        }
        return this;
    }
}
