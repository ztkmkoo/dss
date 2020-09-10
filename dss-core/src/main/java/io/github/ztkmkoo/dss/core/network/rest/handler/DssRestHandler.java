package io.github.ztkmkoo.dss.core.network.rest.handler;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommandHandlerUnregistered;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.DssHandler;
import io.github.ztkmkoo.dss.core.network.rest.entity.DssRestRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:23
 */
@ChannelHandler.Sharable
class DssRestHandler extends SimpleChannelInboundHandler<Object> implements DssHandler<DssRestChannelHandlerCommand> {

    private final Logger logger = LoggerFactory.getLogger(DssRestHandler.class);
    private final AtomicBoolean initializeBehavior = new AtomicBoolean(false);
    private final StringBuilder buffer = new StringBuilder();
    private final Map<String, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<>();
    private final ActorRef<DssRestChannelInitializerCommand> restChannelInitializerActor;
    private final ActorRef<DssRestMasterActorCommand> restMasterActorRef;
    @Getter
    private final String name;
    private final String restMasterActorName;

    private ActorContext<DssRestChannelHandlerCommand> context;
    private HttpRequest request;

    DssRestHandler(
            ActorRef<DssRestChannelInitializerCommand> restChannelInitializerActor,
            ActorRef<DssRestMasterActorCommand> restMasterActorRef,
            String name) {
        this.restChannelInitializerActor = restChannelInitializerActor;
        this.restMasterActorRef = restMasterActorRef;
        this.name = name;
        this.restMasterActorName = restMasterActorRef.path().name();
    }

    private static DssRestRequest dssRestRequest(HttpRequest request, String content) {

        Objects.requireNonNull(request);
        Objects.requireNonNull(content);

        final DssRestMethodType methodType = DssRestMethodType.fromNettyHttpMethod(request.method());
        final DssRestContentType contentType = DssRestContentType.fromText(request.headers().get("content-Type"));
        final String uri = request.uri();

        String boundary = null;
        if (contentType == DssRestContentType.MULTIPART_FORM_DATA) {
          boundary = content.substring(0, content.indexOf("\r\n"));
        }

        return boundary == null ? DssRestRequest
                .builder()
                .methodType(methodType)
                .contentType(contentType)
                .uri(uri)
                .content(content)
                .build()
                :
                DssRestRequest
                .builder()
                .methodType(methodType)
                .contentType(contentType)
                .uri(uri)
                .content(content)
                .boundary(boundary)
                .build();
    }

    private static void sendResponse(
            Map<String, ChannelHandlerContext> channelHandlerContextMap,
            String channelId,
            HttpResponse response) {
        final ChannelHandlerContext ctx = channelHandlerContextMap.remove(channelId);
        if (Objects.nonNull(ctx)) {
            ctx
                    .writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static HttpResponse responseFromServiceActor(DssRestChannelHandlerCommandResponse response) {
        if (Objects.nonNull(response.getResponse())) {
            final ObjectMapper mapper = new ObjectMapper();
            try {
                final String json = mapper.writeValueAsString(response.getResponse());
                return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(response.getStatus()), Unpooled.copiedBuffer(json, CharsetUtil.UTF_8));
            } catch (JsonProcessingException e) {
                return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(response.getStatus()));
        }
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

            if (msg instanceof LastHttpContent) {
                final DssRestRequest dssRestRequest = dssRestRequest(request, buffer.toString());
                handlingDssRestRequest(dssRestRequest, ctx);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void handlingDssRestRequest(DssRestRequest dssRestRequest, ChannelHandlerContext ctx) {
        Objects.requireNonNull(ctx);
        final String channelId = ctx.channel().id().asLongText();
        channelHandlerContextMap.put(channelId, ctx);

        Objects.requireNonNull(context);
        Objects.requireNonNull(restMasterActorRef);
        Objects.requireNonNull(dssRestRequest);

        context.getLog().info("handlingDssRestRequest: {} -> {}", name, restMasterActorName);

        final DssRestMasterActorCommandRequest dssRestMasterActorCommandRequest = DssRestMasterActorCommandRequest
                .builder()
                .channelId(channelId)
                .sender(context.getSelf())
                .methodType(dssRestRequest.getMethodType())
                .contentType(dssRestRequest.getContentType())
                .path(dssRestRequest.getUri())
                .content(dssRestRequest.getContent())
                .boundary(dssRestRequest.getBoundary())
                .charset(dssRestRequest.getCharset())
                .build();

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

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        context.getLog().info("channelUnregistered: {}", name);

        context.scheduleOnce(
                Duration.ofMillis(10000),
                restChannelInitializerActor,
                DssRestChannelInitializerCommandHandlerUnregistered
                        .builder()
                        .name(name)
                        .handlerActor(context.getSelf())
                        .build()
        );
    }

    @Override
    public Behavior<DssRestChannelHandlerCommand> create() {
        if (initializeBehavior.get()) {
            throw new DssUserActorDuplicateBehaviorCreateException("Cannot setup twice for one object");
        }

        initializeBehavior.set(true);
        return Behaviors.setup(this::dssRestHandler);
    }

    private Behavior<DssRestChannelHandlerCommand> dssRestHandler(ActorContext<DssRestChannelHandlerCommand> context) {
        this.context = context;
        context.getLog().info("Setup dssRestHandler: {}", name);

        return Behaviors
                .receive(DssRestChannelHandlerCommand.class)
                .onMessage(DssRestChannelHandlerCommandResponse.class, this::handlingDssRestChannelHandlerCommandResponse)
                .build();
    }

    private Behavior<DssRestChannelHandlerCommand> handlingDssRestChannelHandlerCommandResponse(DssRestChannelHandlerCommandResponse response) {
        context.getLog().info("DssRestChannelHandlerCommandResponse: {}", response);

        final String channelId = response.getChannelId();
        sendResponse(channelHandlerContextMap, channelId, responseFromServiceActor(response));

        return Behaviors.same();
    }
}
