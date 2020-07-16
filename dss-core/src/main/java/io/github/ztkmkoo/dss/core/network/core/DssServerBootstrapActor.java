package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssBehavior;
import io.github.ztkmkoo.dss.core.network.core.entity.DssServerBootstrapProperty;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import io.netty.util.internal.SocketUtils;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 03:29
 */
public class DssServerBootstrapActor extends AbstractDssBehavior<InternalNettyCommand> implements DssServerBootstrap {

    public static Behavior<InternalNettyCommand> create() {
        return Behaviors.setup(DssServerBootstrapActor::new);
    }

    public static Behavior<InternalNettyCommand> create(DssServerBootstrapProperty property) {
        return Behaviors.setup(context -> new DssServerBootstrapActor(context, property));
    }

    private final DssServerBootstrapProperty property;

    private DssServerBootstrapActor(ActorContext<InternalNettyCommand> context) {
        this(context, DssServerBootstrapProperty.DEFAULT_PROPERTY);
    }

    private DssServerBootstrapActor(ActorContext<InternalNettyCommand> context, DssServerBootstrapProperty property) {
        super(context);
        this.property = property;
    }

    @Override
    public Receive<InternalNettyCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(InternalNettyCommand.InitServerBootstrap.class, this::handlingInitServerBootstrap)
                .onMessage(InternalNettyCommand.BossMasterReady.class, this::handlingBossMasterReady)
                .build();
    }

    private Behavior<InternalNettyCommand> handlingInitServerBootstrap(InternalNettyCommand.InitServerBootstrap msg) {
        logMessage(msg);

        final int bossThreadCount = getThreadCount(msg.getBossGroupThread());
        final int workerThreadCount = getThreadCount(msg.getWorkerGroupThread());

        try {
            final ActorRef<InternalNettyCommand> worker = workerMasterActor(workerThreadCount);
            final ActorRef<InternalNettyCommand> boss = bossMasterActor(worker);

            boss.tell(InternalNettyCommand.InitBossMaster
                    .builder()
                    .sender(getSelf())
                    .bossGroupThread(bossThreadCount)
                    .build());
        } catch (Exception e) {
            getLog().error("Some error occurred when initialize server bootstrap actor", e);
            getContext().getSystem().terminate();
        }

        return Behaviors.same();
    }

    private Behavior<InternalNettyCommand> handlingBossMasterReady(InternalNettyCommand.BossMasterReady msg) {
        logMessage(msg);

        final InetSocketAddress localAddress = localAddress(property.getHost(), property.getPort());

        msg.getSender().tell(
                InternalNettyCommand.BossRun
                        .builder()
                        .localAddress(localAddress)
                        .build()
        );

        return Behaviors.same();
    }

    private ActorRef<InternalNettyCommand> workerMasterActor(int workerThreadCount) {
        return getContext()
                .spawn(
                        DssWorkerMasterActor.create(workerThreadCount),
                        DssNetworkCoreContents.DEFAULT_WORKER_MASTER_ACTOR_NAME,
                        DispatcherSelector.fromConfig(DssNetworkCoreContents.WORKER_DISPATCHER_CONFIG)
                );
    }

    private ActorRef<InternalNettyCommand> bossMasterActor(ActorRef<InternalNettyCommand> workerMaster) {
        return getContext()
                .spawn(
                        DssBossMasterActor.create(workerMaster),
                        DssNetworkCoreContents.DEFAULT_BOSS_MASTER_ACTOR_NAME
                );
    }

    private static int getThreadCount(Integer count) {
        if (Objects.nonNull(count) && count > 0) {
            return count;
        }

        return 0;
    }

    private static InetSocketAddress localAddress(String host, int port) {
        if (StringUtils.isEmpty(host)) {
            return new InetSocketAddress(port);
        }

        return SocketUtils.socketAddress(host, port);
    }
}
