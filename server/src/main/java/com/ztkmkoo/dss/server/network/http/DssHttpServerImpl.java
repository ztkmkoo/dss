package com.ztkmkoo.dss.server.network.http;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.AbstractDssServer;
import com.ztkmkoo.dss.server.network.core.DssServerProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Optional;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
@Deprecated
public class DssHttpServerImpl extends AbstractDssServer {

    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";

    @Override
    protected ServerBootstrap configServerBootstrap(DssServerProperty p, ServerBootstrap b, ActorRef<ServerMessages.Req> masterActor) throws InterruptedException {

        if (p instanceof DssHttpServerProperty) {

            final DssHttpServerProperty property = (DssHttpServerProperty) p;

            final Optional<SslContext> sslContext = sslContextOptional(property);
            final String scheme = sslContext.isPresent() ? SCHEME_HTTPS : SCHEME_HTTP;

            b
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(sslContext
                            .map(context -> new DssHttpServerInitializer(masterActor, context, property.getHandlerCreator()))
                            .orElseGet(() -> new DssHttpServerInitializer(masterActor, property.getHandlerCreator())));

            logger.info("Http(s) server is binding to {}://{}:{}", scheme, property.getHost(), property.getPort());

            return b;

        } else {
            throw new InterruptedException("DssServerProperty invalid type. cannot set ssl context. " + p.toString());
        }
    }

    private Optional<SslContext> sslContextOptional(DssServerProperty property) {

        final DssHttpServerProperty httpServerProperty = (DssHttpServerProperty) property;
        if (!httpServerProperty.isSsl()) {
            return Optional.empty();
        }

        try {
            final SelfSignedCertificate ssc = new SelfSignedCertificate();
            return Optional.of(SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build());
        } catch (CertificateException | SSLException e) {
            logger.warn("Create SelfSignedCertificate error: ", e);
        }

        return Optional.empty();
    }
}
