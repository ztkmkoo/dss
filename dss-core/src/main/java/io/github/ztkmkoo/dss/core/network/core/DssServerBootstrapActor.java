package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssBehavior;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 03:29
 */
public class DssServerBootstrapActor extends AbstractDssBehavior<InternalNettyCommand> implements DssServerBootstrap {

    public static Behavior<InternalNettyCommand> create() {
        return Behaviors.setup(DssServerBootstrapActor::new);
    }

    private DssServerBootstrapActor(ActorContext<InternalNettyCommand> context) {
        super(context);
    }

    @Override
    public Receive<InternalNettyCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(InternalNettyCommand.InitServerBootstrap.class, this::handlingInitServerBootstrap)
                .onMessage(InternalNettyCommand.BossMasterReady.class, this::handlingBossMasterReady)
                .build();
    }

    private Behavior<InternalNettyCommand> handlingInitServerBootstrap(InternalNettyCommand.InitServerBootstrap msg) {
        getLog().debug("Receive: {}", msg);

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
        getLog().debug("Receive: {}", msg);
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
}
