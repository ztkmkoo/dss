package io.github.ztkmkoo.dss.core.network.tcp.handler;

import java.security.cert.CertificateException;
import java.util.Objects;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class DssTcpSslChannelInitializer extends DssTcpChannelInitializer {

    private final Logger logger = LoggerFactory.getLogger(DssTcpSslChannelInitializer.class);

    private final SslContext sslCtx;

    public DssTcpSslChannelInitializer(SslContext sslContext) throws InterruptedException {
        this.sslCtx = (Objects.nonNull(sslContext)) ? sslContext : selfSignedSslContext();
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(sslCtx.newHandler(ch.alloc()));
        p.addLast(new ByteArrayDecoder());
        p.addLast(new ByteArrayEncoder());
        p.addLast(new DssTcpHandler());
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

