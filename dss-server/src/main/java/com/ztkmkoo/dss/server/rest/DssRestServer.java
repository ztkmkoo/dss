package com.ztkmkoo.dss.server.rest;

import akka.actor.typed.ActorSystem;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import com.ztkmkoo.dss.core.network.rest.DssRestChannel;
import com.ztkmkoo.dss.core.network.rest.handler.DssRestChannelInitializer;
import com.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 1:47
 */
public class DssRestServer {

    private final Logger logger = LoggerFactory.getLogger(DssRestServer.class);
    private final String host;
    private final int port;
    private final List<DssRestActorService> serviceList = new ArrayList<>();

    private Channel channel;
    private ActorSystem<DssRestChannelInitializerCommand> system;

    public DssRestServer(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public DssRestServer addDssRestService(DssRestActorService service) {
        Objects.requireNonNull(service);
        this.serviceList.add(service);
        return this;
    }

    public void start() throws InterruptedException {
        final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(serviceList);
        system = createActorSystem(channelInitializer);
        logger.info("Create actor system: {}", system);

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssRestChannelProperty property = newDssRestChannelProperty();

        try {
            final DssRestChannel dssRestChannel = new DssRestChannel();
            channel = dssRestChannel.bind(bootstrap, property, channelInitializer);
            channel.closeFuture().sync();
        } finally {
            logger.info("Shut down worker and boss group gracefully");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private ActorSystem<DssRestChannelInitializerCommand> createActorSystem(DssRestChannelInitializer channelInitializer) {
        final ActorSystem<DssRestChannelInitializerCommand> newSystem = ActorSystem.create(channelInitializer.create(), "system");
        newSystem.getWhenTerminated().toCompletableFuture().thenAccept(done -> logger.info("Actor system terminated."));
        return newSystem;
    }

    public void stop() throws InterruptedException {
        if (Objects.nonNull(channel)) {
            logger.info("Channel try to close. [Active: {}][Open: {}]", channel.isActive(), channel.isOpen());
            channel.close();
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
                .build();
    }
}
