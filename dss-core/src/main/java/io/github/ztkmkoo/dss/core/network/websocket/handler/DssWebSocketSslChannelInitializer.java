package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DssWebSocketSslChannelInitializer extends DssWebSocketChannelInitializer {

    private final Logger logger = LoggerFactory.getLogger(DssWebSocketSslChannelInitializer.class);

    private final String WEBSOCKET_PATH = "/websocket";
    private final SslContext sslCtx;

    public DssWebSocketSslChannelInitializer(SslContext sslCtx) throws InterruptedException {
        this.sslCtx = Objects.nonNull(sslCtx) ? sslCtx : selfSignedSslContext();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(sslCtx.newHandler(ch.alloc()));

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new DssHttpRequestHandler(WEBSOCKET_PATH));
        p.addLast(new DssWebSocketHandler());
    }

    private static SslContext selfSignedSslContext() throws InterruptedException {
        try {
            final SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (CertificateException | SSLException e) {
            throw new InterruptedException("Create self signed ssl context failed");
        }
    }
}
