package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelInitializerProperty;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-22 07:25
 */
public class DssDefaultRestSslChannelInitializer extends DssDefaultRestChannelInitializer {

    private static SslContext selfSignedSslContext() throws InterruptedException {
        try {
            final SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (CertificateException | SSLException e) {
            throw new InterruptedException("Create self signed ssl context failed");
        }
    }

    private final SslContext sslCtx;

    public DssDefaultRestSslChannelInitializer(ActorRef<DssResolverCommand> resolverActor, DssRestChannelInitializerProperty property) throws InterruptedException {
        super(resolverActor);
        this.sslCtx = Objects.nonNull(property.getSslCtx()) ? property.getSslCtx() : selfSignedSslContext();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ChannelPipeline p = ch.pipeline();
        p.addLast(sslCtx.newHandler(ch.alloc()));
        super.initRestChannel(p);
    }
}
