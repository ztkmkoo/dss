package com.ztkmkoo.dss.network.http;

import com.ztkmkoo.dss.network.common.creator.DssServerHandlerCreator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
class DssHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final boolean ssl;
    private final SslContext sslContext;
    private final DssServerHandlerCreator handlerCreator;

    DssHttpServerInitializer(
            boolean ssl,
            SslContext sslContext,
            DssServerHandlerCreator handlerCreator) {
        this.ssl = ssl;
        this.sslContext = sslContext;
        this.handlerCreator = handlerCreator;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        final ChannelPipeline p = ch.pipeline();

        if (ssl) {
            Objects.requireNonNull(sslContext);
            p.addLast(sslContext.newHandler(ch.alloc()));
        }

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(handlerCreator.createChannelHandler());
    }
}
