package io.github.ztkmkoo.dss.server.websocket;

import akka.actor.ActorSystem;
import io.github.ztkmkoo.dss.core.actor.DssActorService;
import io.github.ztkmkoo.dss.core.exception.handler.DssExceptionHandler;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssLogLevel;
import io.github.ztkmkoo.dss.core.network.websocket.DssWebSocketChannel;
import io.github.ztkmkoo.dss.core.network.websocket.handler.DssWebSocketChannelInitializer;
import io.github.ztkmkoo.dss.core.network.websocket.handler.DssWebSocketSslChannelInitializer;
import io.github.ztkmkoo.dss.core.network.websocket.property.DssWebSocketChannelProperty;
import io.github.ztkmkoo.dss.server.DssServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DssWebSocketServer implements DssServer {

    private final DssLogLevel dssLogLevel;
    private final Logger logger = LoggerFactory.getLogger(DssWebSocketServer.class);
    private final String host;
    private final int port;
    private final boolean ssl;
    private final SslContext sslContext;
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final AtomicBoolean shutdown = new AtomicBoolean(true);

    private Channel channel;
    private ActorSystem system;

    public DssWebSocketServer(String host, int port) { this(host, port, DssLogLevel.DEBUG, false,null); }

    public DssWebSocketServer(String host, int port, DssLogLevel dssLogLevel) { this(host, port, dssLogLevel, false, null); }

    public DssWebSocketServer(String host, int port, DssLogLevel dssLogLevel, boolean ssl, SslContext sslContext) {
        this.host = host;
        this.port = port;
        this.dssLogLevel = Objects.nonNull(dssLogLevel) ? dssLogLevel : DssLogLevel.DEBUG;
        this.ssl = ssl;
        this.sslContext = sslContext;
    }

    @Override
    public void start() throws InterruptedException {
        final DssWebSocketChannelInitializer channelInitializer = dssWebsocketChannelInitializer();

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssWebSocketChannelProperty property = newDssWebsocketChannelProperty();

        try {
            final DssWebSocketChannel dssWebsocketChannel = new DssWebSocketChannel();
            channel = dssWebsocketChannel.bind(bootstrap, property, channelInitializer);
            active.set(true);
            shutdown.set(false);
            logger.info("Server Started");
            channel.closeFuture().sync();
        } finally {
            logger.info("Shut down worker and boss group gracefully");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            shutdown.set(true);
        }

    }

    @Override
    public void stop() throws InterruptedException {
        if (Objects.nonNull(channel)) {
            logger.info("Channel try to close. [Active: {}][Open: {}]", channel.isActive(), channel.isOpen());
            channel.close();
            active.set(false);
        }

        if (Objects.nonNull(system)) {
            logger.info("Actor system try to terminate");
            system.terminate();

            final int waitMaxSecond = 10;
            int count = 0;
            while (count++ < waitMaxSecond) {
                Thread.sleep(1000);
                if (system.whenTerminated().isCompleted()) {
                    break;
                }
            }

            if (!system.whenTerminated().isCompleted()) {
                logger.error("Cannot terminate actor system.");
            }
        }
    }

    @Override
    public boolean isActivated() {
        return active.get();
    }

    @Override
    public boolean isShutdown() {
        return shutdown.get();
    }

    @Override
    public DssServer addDssService(DssActorService service) {
        return null;
    }

    @Override
    public DssServer addExceptionHandler(DssExceptionHandler exceptionHandler) { return null; }

    private DssWebSocketChannelProperty newDssWebsocketChannelProperty() {
        return DssWebSocketChannelProperty
                .builder()
                .host(host)
                .port(port)
                .dssLogLevel(dssLogLevel)
                .build();
    }

    private DssWebSocketChannelInitializer dssWebsocketChannelInitializer() throws InterruptedException {
        return ssl ? new DssWebSocketSslChannelInitializer(sslContext) : new DssWebSocketChannelInitializer();
    }
}
