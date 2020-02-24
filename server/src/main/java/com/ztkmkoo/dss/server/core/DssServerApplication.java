package com.ztkmkoo.dss.server.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.AskPattern;
import com.ztkmkoo.dss.server.actor.core.DssServerActorProperty;
import com.ztkmkoo.dss.server.actor.core.DssServerMasterActor;
import com.ztkmkoo.dss.server.core.exception.DssServerApplicationRuntimeException;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.DssNetworkChannel;
import com.ztkmkoo.dss.server.network.core.DssNetworkChannelProperty;
import com.ztkmkoo.dss.server.network.rest.DssRestChannel;
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
        run(propertyList, system);
    }

    public static void run(List<DssServerApplicationProperty> propertyList, ActorSystem<SpawnProtocol.Command> system) {
        propertyList.forEach(property -> handlingDssServerApplicationProperty(property, system));
    }

    public static void run(DssServerApplicationProperty property) {
        Objects.requireNonNull(property);
        run(Collections.singletonList(property));
    }

    public static void run(DssServerApplicationProperty property, ActorSystem<SpawnProtocol.Command> system) {
        Objects.requireNonNull(property);
        run(Collections.singletonList(property), system);
    }

    private static void handlingDssServerApplicationProperty(DssServerApplicationProperty property, ActorSystem<SpawnProtocol.Command> system) {

        final DssNetworkType networkType = property.getNetworkType();
        Objects.requireNonNull(networkType);

        if (DssNetworkType.REST.equals(networkType)) {
            askForActorAndDssChannelBind(system, property, new DssRestChannel());
        }
    }

    @SuppressWarnings("unchcked")
    private static void askForActorAndDssChannelBind(ActorSystem<SpawnProtocol.Command> system, DssServerApplicationProperty property, DssNetworkChannel networkChannel) {

        AskPattern
                .ask(
                        system,
                        replyTo -> newSpawnProtocol(replyTo, property.getDssServerActorProperty()),
                        Duration.ofSeconds(3),
                        system.scheduler()
                )
                .whenComplete((o, throwable) -> {
                    try {
                        Objects.requireNonNull(o);
                        if (o instanceof ActorRef) {
                            // CHECK - need new thread?
                            serverBind(property.getNetworkProperty(), networkChannel, (ActorRef<ServerMessages.Req>)o);
                        } else {
                            throw new DssServerApplicationRuntimeException("Akka spawn new actor error: " + o.getClass().getName());
                        }
                    } catch (Exception e) {
                        logger.error("Server bind error: ", e);
                        Thread.currentThread().interrupt();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private static SpawnProtocol.Spawn<ActorRef<ServerMessages.Req>> newSpawnProtocol(ActorRef<Object> replyTo, DssServerActorProperty dssServerActorProperty) {

        return new SpawnProtocol.Spawn(
                dssServerActorProperty.getMasterBehavior(),
                dssServerActorProperty.getMasterName(),
                Props.empty(),
                replyTo
        );
    }

    private static void serverBind(DssNetworkChannelProperty property, DssNetworkChannel networkChannel, ActorRef<ServerMessages.Req> requestActorRef) throws InterruptedException {

        final EventLoopGroup boosGroup = new NioEventLoopGroup(property.getBossThread());
        final EventLoopGroup workerGroup = new NioEventLoopGroup(property.getWorkerThread());

        try {
            final ServerBootstrap b = new ServerBootstrap().group(boosGroup, workerGroup);
            final Channel channel = networkChannel.bind(b, property, requestActorRef);

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
