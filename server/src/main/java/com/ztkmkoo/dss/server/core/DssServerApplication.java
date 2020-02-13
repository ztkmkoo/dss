package com.ztkmkoo.dss.server.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.AskPattern;
import com.ztkmkoo.dss.server.actor.core.DssServerMasterActor;
import com.ztkmkoo.dss.server.core.exception.DssServerApplicationRuntimeException;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.DssServerProperty;
import com.ztkmkoo.dss.server.util.CollectionUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:37
 */
public class DssServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(DssServerApplication.class);

    private DssServerApplication() {}

    public static void run(List<DssServerApplicationProperty> propertyList) {

        if (CollectionUtils.List.isEmpty(propertyList)) {
            throw new DssServerApplicationRuntimeException("Server cannot start without any property.");
        }

        final ActorSystem<SpawnProtocol.Command> system = ActorSystem.create(DssServerMasterActor.create(), "system");
        propertyList.forEach(property -> handlingDssServerApplicationProperty(property, system));
    }

    public static void run(DssServerApplicationProperty property) {
        Objects.requireNonNull(property);
        run(Collections.singletonList(property));
    }

    private static void handlingDssServerApplicationProperty(DssServerApplicationProperty property, ActorSystem<SpawnProtocol.Command> system) {

        final DssNetworkType networkType = property.getNetworkProperty().getNetworkType();

        CompletionStage<ActorRef<ServerMessages.Req>> cs = AskPattern
                .ask(
                        system,
                        networkType::spawnMessage,
                        Duration.ofSeconds(3),
                        system.scheduler()
                );

        cs.whenComplete((requestActorRef, throwable) -> {
            try {
                serverBind(property, networkType, requestActorRef);
            } catch (InterruptedException e) {
                logger.error("Server bind error: ", e);
                Thread.currentThread().interrupt();
            }
        });
    }

    private static void serverBind(DssServerApplicationProperty property, DssNetworkType networkType, ActorRef<ServerMessages.Req> masterActor) throws InterruptedException {

        final DssServerProperty serverProperty = property.getNetworkProperty();

        final EventLoopGroup boosGroup = new NioEventLoopGroup(serverProperty.getBossThread());
        final EventLoopGroup workerGroup = new NioEventLoopGroup(serverProperty.getWorkerThread());

        try {
            final ServerBootstrap b = new ServerBootstrap().group(boosGroup, workerGroup);
            final Channel channel = networkType.getCreator().create().bind(b, serverProperty, masterActor);

            Objects.requireNonNull(channel);

            channel
                    .closeFuture()
                    .sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
