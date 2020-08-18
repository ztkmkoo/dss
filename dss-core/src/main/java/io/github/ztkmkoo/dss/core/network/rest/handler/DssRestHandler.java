package io.github.ztkmkoo.dss.core.network.rest.handler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import io.github.ztkmkoo.dss.core.message.rest.*;
import io.github.ztkmkoo.dss.core.network.DssHandler;
import io.github.ztkmkoo.dss.core.network.rest.entity.DssRestRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:23
 */
@ChannelHandler.Sharable
class DssRestHandler extends SimpleChannelInboundHandler<Object> implements DssHandler<DssRestChannelHandlerCommand> {

    private static final String REGEX_CONTENT_CHARSET = " charset\\=.*";

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
                sendRequestToService(ctx, request, content);
            }
        }
    }

    private void sendRequestToService(ChannelHandlerContext ctx, HttpRequest request, ByteBuf content) {
        Objects.requireNonNull(ctx);
        final String channelId = ctx.channel().id().asLongText();
        channelHandlerContextMap.put(channelId, ctx);

        Objects.requireNonNull(context);
        Objects.requireNonNull(restMasterActorRef);
        Objects.requireNonNull(request);
        Objects.requireNonNull(content);

        context.getLog().info("sendRequestToService: {} -> {}", name, restMasterActorName);

        final DssRestRequestCommand.Builder builder = DssRestRequestCommand.builder(channelId, context.getSelf());

        final DssRestMethodType methodType = DssRestMethodType.fromNettyHttpMethod(request.method());
        final String uri = request.uri();
        final byte[] contents = content.array();

        builder
                .methodType(methodType)
                .path(uri)
                .content(contents);

        final String fullContentTypeString = request.headers().get("content-Type");
        if (!StringUtils.isEmpty(fullContentTypeString)) {
            final String[] contentTypeSplits = fullContentTypeString.split(";");
            final DssRestContentType contentType = DssRestContentType.fromText(contentTypeSplits[0]);
            builder.contentType(contentType);

            if (contentTypeSplits.length > 1) {
                final String contentExtra = contentTypeSplits[1];
                if (contentExtra.matches(REGEX_CONTENT_CHARSET)) {
                    final String charset = contentExtra.substring(contentExtra.indexOf("=") + 1);
                    try {
                        final Charset c = Charset.forName(charset);
                        builder.charset(c.name());
                    } catch (UnsupportedCharsetException e) {
                        logger.warn("UnsupportedCharsetException", e);
                    }
                }
            }
        }

        restMasterActorRef.tell(builder.build());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * @deprecated use sendRequestToService instead
     */
    @Deprecated
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
