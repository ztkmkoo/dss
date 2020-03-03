package com.ztkmkoo.dss.core.network.rest.handler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import com.ztkmkoo.dss.core.actor.rest.DssRestMasterActor;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import com.ztkmkoo.dss.core.network.rest.entity.DssRestRequest;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:23
 */
class DssRestHandler extends SimpleChannelInboundHandler<Object> {

    private final Logger logger = LoggerFactory.getLogger(DssRestHandler.class);
    private final AtomicBoolean initializeBehavior = new AtomicBoolean(false);
    private final StringBuilder buffer = new StringBuilder();
    private final Map<String, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<>();

    private ActorContext<DssRestChannelHandlerCommand> context;
    private ActorRef<DssRestMasterActorCommand> restMasterActorRef;
    private HttpRequest request;

    DssRestHandler() {}

    DssRestHandler(ActorRef<DssRestMasterActorCommand> restMasterActorRef) {
        this.restMasterActorRef = restMasterActorRef;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

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
        handlingDssRestRequest(dssRestRequest, ctx);
    }

    private void handlingDssRestRequest(DssRestRequest dssRestRequest, ChannelHandlerContext ctx) {

        Objects.requireNonNull(context);
        Objects.requireNonNull(restMasterActorRef);
        Objects.requireNonNull(dssRestRequest);
        Objects.requireNonNull(ctx);

        final String channelId = ctx.channel().id().asLongText();

        final DssRestMasterActorCommandRequest dssRestMasterActorCommandRequest = DssRestMasterActorCommandRequest
                .builder()
                .channelId(channelId)
                .sender(context.getSelf())
                .build();

        channelHandlerContextMap.put(channelId, ctx);
        restMasterActorRef.tell(dssRestMasterActorCommandRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        logger.error("exceptionCaught", cause);

        final String channelId = ctx.channel().id().asLongText();
        if (Objects.isNull(channelHandlerContextMap.remove(channelId))) {
            // already removed.
            return;
        }

        ctx.close();
    }

    public Behavior<DssRestChannelHandlerCommand> create() {

        if (initializeBehavior.get()) {
            throw new DssUserActorDuplicateBehaviorCreateException("Cannot setup twice for one object");
        }

        initializeBehavior.set(true);
        return Behaviors.setup(this::dssRestHandler);
    }

    private Behavior<DssRestChannelHandlerCommand> dssRestHandler(ActorContext<DssRestChannelHandlerCommand> context) {

        this.context = context;
        if (Objects.isNull(this.restMasterActorRef)) {
            this.restMasterActorRef = this.context.spawn(DssRestMasterActor.create(), "rest-master");
        }

        return Behaviors
                .receive(DssRestChannelHandlerCommand.class)
                .onMessage(DssRestChannelHandlerCommandResponse.class, this::handlingDssRestChannelHandlerCommandResponse)
                .build();
    }

    private Behavior<DssRestChannelHandlerCommand> handlingDssRestChannelHandlerCommandResponse(DssRestChannelHandlerCommandResponse response) {

        context.getLog().info("DssRestChannelHandlerCommandResponse: {}", response);

        final String channelId = response.getChannelId();

        final ChannelHandlerContext ctx = channelHandlerContextMap.remove(channelId);
        if (Objects.nonNull(ctx)) {
            ctx
                    .writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK))
                    .addListener(ChannelFutureListener.CLOSE);
        }

        return Behaviors.same();
    }

    private static DssRestRequest dssRestRequest(HttpRequest request, String content) {

        Objects.requireNonNull(request);
        Objects.requireNonNull(content);

        final DssRestMethodType methodType = DssRestMethodType.fromNettyHttpMethod(request.method());
        final String uri = request.uri();

        return DssRestRequest
                .builder()
                .methodType(methodType)
                .uri(uri)
                .content(content)
                .build();
    }
}
