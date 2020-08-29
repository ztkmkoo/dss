package io.github.ztkmkoo.dss.server.rest;

import akka.actor.typed.ActorSystem;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssLogLevel;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.exception.handler.DssExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.handler.DssRestExceptionHandlerResolver;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import io.github.ztkmkoo.dss.core.network.rest.DssRestChannel;
import io.github.ztkmkoo.dss.core.network.rest.handler.DssRestChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.handler.DssRestSslChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.github.ztkmkoo.dss.server.DssServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 1:47
 */
public class DssRestServer implements DssServer<DssRestActorService> {

    private final DssLogLevel dssLogLevel;
    private final Logger logger = LoggerFactory.getLogger(DssRestServer.class);
    private final String host;
    private final int port;
    private final boolean ssl;
    private final SslContext sslContext;
    private final List<DssRestActorService> serviceList = new ArrayList<>();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final AtomicBoolean shutdown = new AtomicBoolean(true);

    private Channel channel;
    private ActorSystem<DssRestChannelInitializerCommand> system;
    private DssExceptionHandler exceptionHandler;

    public DssRestServer(String host, int port) {
        this(host, port, DssLogLevel.DEBUG ,false, null);
    }

    public DssRestServer(String host, int port, DssLogLevel dssLogLevel) {
        this(host, port, dssLogLevel, false, null);
    }

    public DssRestServer(String host, int port, DssLogLevel dssLogLevel, boolean ssl, SslContext sslContext) {
        this.host = host;
        this.port = port;
        this.dssLogLevel = Objects.nonNull(dssLogLevel) ? dssLogLevel : DssLogLevel.DEBUG;
        this.ssl = ssl;
        this.sslContext = sslContext;
    }

    /**
     * @deprecated use addDssService
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public DssRestServer addDssRestService(DssRestActorService service) {
        return (DssRestServer)addDssService(service);
    }

    @Override
    public void start() throws InterruptedException {
        final DssRestChannelInitializer channelInitializer = dssRestChannelInitializer();
        system = createActorSystem(channelInitializer);
        logger.info("Create actor system: {}", system);

        setExceptionHandler();

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssRestChannelProperty property = newDssRestChannelProperty();

        try {
            final DssRestChannel dssRestChannel = new DssRestChannel();
            channel = dssRestChannel.bind(bootstrap, property, channelInitializer);
            active.set(true);
            shutdown.set(false);
            channel.closeFuture().sync();
        } finally {
            logger.info("Shut down worker and boss group gracefully");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            shutdown.set(true);
        }
    }

    private ActorSystem<DssRestChannelInitializerCommand> createActorSystem(DssRestChannelInitializer channelInitializer) {
        final ActorSystem<DssRestChannelInitializerCommand> newSystem = ActorSystem.create(channelInitializer.create(), "system");
        newSystem.getWhenTerminated().toCompletableFuture().thenAccept(done -> logger.info("Actor system terminated."));
        return newSystem;
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

    private DssRestChannelProperty newDssRestChannelProperty() {
        return DssRestChannelProperty
                .builder()
                .host(host)
                .port(port)
                .dssLogLevel(dssLogLevel)
                .build();
    }

    private DssRestChannelInitializer dssRestChannelInitializer() throws InterruptedException {
        return ssl ? new DssRestSslChannelInitializer(serviceList, sslContext) : new DssRestChannelInitializer(serviceList);
    }

    private void setExceptionHandler() {
        if (Objects.nonNull(this.exceptionHandler)){
            DssRestExceptionHandlerResolver.getInstance().setExceptionHandlerMap(this.exceptionHandler);
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
    public DssServer<DssRestActorService> addDssService(DssRestActorService service) {
        Objects.requireNonNull(service);
        this.serviceList.add(service);
        return this;
    }

    @Override
    public DssServer<DssRestActorService> addExceptionHandler(DssExceptionHandler exceptionHandler) {
        Objects.requireNonNull(exceptionHandler);
        this.exceptionHandler = exceptionHandler;
        return this;
    }
}
