package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.network.DssNettyChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.rest.entity.DssDefaultRestRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-22 07:24
 */
public class DssDefaultRestChannelHandler extends SimpleChannelInboundHandler<Object> implements DssRestChannelHandler {

    private final StringBuilder buffer = new StringBuilder();
    private final ActorRef<DssResolverCommand> resolverActor;

    private HttpRequest httpRequest;

    public DssDefaultRestChannelHandler(ActorRef<DssResolverCommand> resolverActor) {
        this.resolverActor = Objects.requireNonNull(resolverActor);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            buffer.setLength(0);
            this.httpRequest = (HttpRequest) msg;
        }

        if (msg instanceof HttpContent) {
            final HttpContent httpContent = (HttpContent) msg;
            final ByteBuf content = httpContent.content();

            if (content.isReadable()) {
                buffer.append(content.toString(CharsetUtil.UTF_8));
            }

            if (msg instanceof LastHttpContent) {
                final DssNettyChannelHandlerContext context = new DssNettyChannelHandlerContext(ctx);
                final DssDefaultRestRequest request = new DssDefaultRestRequest(httpRequest);
                final DssRestResolverCommand.RestRequest restRequest = makeRequestCommand(context, request, buffer.toString());
                resolverActor.tell(restRequest);
            }
        }
    }
}
