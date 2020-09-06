package io.github.ztkmkoo.dss.core.network.rest.handler;

import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 6. 11. 오전 2:11
 */
public class DssRestSslChannelInitializer extends DssRestChannelInitializer {

    private static SslContext selfSignedSslContext() throws InterruptedException {
        try {
            final SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (CertificateException | SSLException e) {
            throw new InterruptedException("Create self signed ssl context failed");
        }
    }

    private final Logger logger = LoggerFactory.getLogger(DssRestSslChannelInitializer.class);
    private final SslContext sslCtx;

    public DssRestSslChannelInitializer(List<DssRestActorService> serviceList) throws InterruptedException {
        this(serviceList, selfSignedSslContext());
    }

    public DssRestSslChannelInitializer(List<DssRestActorService> serviceList, SslContext sslContext) throws InterruptedException {
        super(serviceList);
        this.sslCtx = (Objects.nonNull(sslContext)) ? sslContext : selfSignedSslContext();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");
        final ChannelPipeline p = ch.pipeline();

        p.addLast(sslCtx.newHandler(ch.alloc()));
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());

        super.addHandler(p);
    }
}
