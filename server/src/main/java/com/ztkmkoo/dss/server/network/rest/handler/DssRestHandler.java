package com.ztkmkoo.dss.server.network.rest.handler;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestErrorResponses;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestRequest;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestResponse;
import com.ztkmkoo.dss.server.network.rest.enumeration.DssRestMethod;
import com.ztkmkoo.dss.server.network.rest.service.DssResHandlerService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 1:21
 */
public class DssRestHandler extends SimpleChannelInboundHandler<Object> {

    protected final Logger logger = LoggerFactory.getLogger(DssRestHandler.class);
    private final StringBuilder buffer = new StringBuilder();
    private final ActorRef<ServerMessages.Req> masterActorRef;
    @Getter
    private final Map<String, DssResHandlerService> simpleHandlerServiceMap = new HashMap<>();

    private HttpRequest request;

    public DssRestHandler(ActorRef<ServerMessages.Req> masterActorRef) {
        this.masterActorRef = masterActorRef;
    }

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

        if (isInSimpleHandlerService(dssRestRequest)) {
            final DssRestResponse dssRestResponse = simpleHandlerServiceMap
                    .get(dssRestRequest.getUri())
                    .handle(dssRestRequest);
            final HttpResponse response = dssRestResponse(dssRestResponse);
            ctx
                    .writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx
                    .writeAndFlush(new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.BAD_REQUEST,
                            Unpooled.EMPTY_BUFFER
                    ))
                    .addListener(ChannelFutureListener.CLOSE);
        }
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

    private boolean isInSimpleHandlerService(DssRestRequest request) {
        return simpleHandlerServiceMap.containsKey(request.getUri());
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

    private static HttpResponse dssRestResponse(DssRestResponse restResponse) {

        Objects.requireNonNull(restResponse);

        try {
            final ByteBuf responseContent = responseContent(restResponse);
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    responseContent
            );
        } catch (Exception e) {
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    Unpooled.EMPTY_BUFFER
            );
        }
    }

    private static ByteBuf responseContent(DssRestResponse restResponse) throws JsonProcessingException {

        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(restResponse);
        return Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
    }
}
