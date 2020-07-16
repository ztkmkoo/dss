package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssBehavior;
import io.github.ztkmkoo.dss.core.network.core.util.NetworkCoreUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 05:08
 */
public class DssBossMasterActor extends AbstractDssBehavior<InternalNettyCommand> implements DssMultiThreadEventLoopGroup {

    public static Behavior<InternalNettyCommand> create(ActorRef<InternalNettyCommand> workerMaster) {
        return Behaviors.setup(context -> new DssBossMasterActor(context, workerMaster));
    }

    private final ActorRef<InternalNettyCommand> workerMaster;
    private final Map<String, ActorRef<InternalNettyCommand>> bossMap = new HashMap<>();

    private DssBossMasterActor(ActorContext<InternalNettyCommand> context, ActorRef<InternalNettyCommand> workerMaster) {
        super(context);
        this.workerMaster = workerMaster;
    }

    @Override
    public Receive<InternalNettyCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(InternalNettyCommand.InitBossMaster.class, this::handlingInitBossMaster)
                .onMessage(InternalNettyCommand.BossRun.class, this::handlingBossRun)
                .build();
    }

    private Behavior<InternalNettyCommand> handlingInitBossMaster(InternalNettyCommand.InitBossMaster msg) {
        logMessage(msg);

        final int bossThreadCount = NetworkCoreUtils.getThreadsCount(msg.getBossGroupThread());

        try {
            for (int i = 0; i < bossThreadCount; i++) {
                final String name = getBossActorNameByIndex(i);
                getLog().debug("Try to initialize {}", name);
                final ActorRef<InternalNettyCommand> boss = getContext().spawn(DssBossActor.create(workerMaster), name);
                bossMap.put(name, boss);
            }
        } catch (Exception e) {
            getLog().error("Some error occurred when initialize DssBossActor", e);
            getContext().getSystem().terminate();
        }

        getLog().info("BossMasterActor ready.");
        msg.getSender().tell(InternalNettyCommand.BossMasterReady
                .builder()
                .sender(getSelf())
                .build()
        );

        return Behaviors.same();
    }

    private Behavior<InternalNettyCommand> handlingBossRun(InternalNettyCommand.BossRun msg) {
        logMessage(msg);
        for (ActorRef<InternalNettyCommand> boss : bossMap.values()) {
            boss.tell(msg);
        }
        return Behaviors.same();
    }

    private static String getBossActorNameByIndex(int index) {
        return DssNetworkCoreContents.DEFAULT_BOSS_ACTOR_NAME_PREFIX + index;
    }
}
