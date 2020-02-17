package com.ztkmkoo.dss.server.network.http;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.creator.DssServerHandlerCreator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
class DssHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Consumer<SocketChannel> NON_SSL_CONSUMER = channel -> {};

    private final ActorRef<ServerMessages.Req> masterActor;
    private final DssServerHandlerCreator handlerCreator;
    private final Consumer<SocketChannel> socketChannelConsumer;

    DssHttpServerInitializer(ActorRef<ServerMessages.Req> masterActor, DssServerHandlerCreator handlerCreator) {
        this(masterActor, handlerCreator, NON_SSL_CONSUMER);
    }

    DssHttpServerInitializer(
            ActorRef<ServerMessages.Req> masterActor,
            SslContext sslContext,
            DssServerHandlerCreator handlerCreator) {
        this(masterActor, handlerCreator, channel -> {
            Objects.requireNonNull(sslContext);
            final ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(sslContext.newHandler(channel.alloc()));
        });
    }

    DssHttpServerInitializer(
            ActorRef<ServerMessages.Req> masterActor,
            DssServerHandlerCreator handlerCreator,
            Consumer<SocketChannel> socketChannelConsumer
    ) {
        this.masterActor = masterActor;
        this.handlerCreator = handlerCreator;
        this.socketChannelConsumer = socketChannelConsumer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        final ChannelPipeline p = ch.pipeline();

        socketChannelConsumer.accept(ch);

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(handlerCreator.createChannelHandler(masterActor));
    }
}
