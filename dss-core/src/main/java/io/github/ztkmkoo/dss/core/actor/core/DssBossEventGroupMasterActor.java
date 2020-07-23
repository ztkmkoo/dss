package io.github.ztkmkoo.dss.core.actor.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.internal.BehaviorImpl;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.message.core.DssInternalSystemCommand;
import io.netty.util.internal.ObjectUtil;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-22 23:41
 */
final class DssBossEventGroupMasterActor extends AbstractDssCoreActor<DssInternalSystemCommand> implements DssEventGroupActor {

    public static Behavior<DssInternalSystemCommand> create() {
        return AbstractDssCoreActor.create(Behaviors.setup(DssBossEventGroupMasterActor::new));
    }

    private static final int MAX_LISTEN_ERROR_COUNT = 5;

    private final Set<ActorRef<DssInternalSystemCommand>> bossSet = new HashSet<>(1);

    private int listenErrorCount;

    private DssBossEventGroupMasterActor(ActorContext<DssInternalSystemCommand> context) {
        super(context);

        this.listenErrorCount = 0;
    }

    @Override
    public Receive<DssInternalSystemCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(DssInternalSystemCommand.BossMasterInitialize.class, this::handlingBossMasterInitialize)
                .onMessage(DssInternalSystemCommand.ServerListen.class, this::handlingServerListen)
                .build();
    }

    private Behavior<DssInternalSystemCommand> handlingBossMasterInitialize(DssInternalSystemCommand.BossMasterInitialize msg) {
        if (!bossSet.isEmpty()) {
            bossSet.clear();
        }

        final ActorRef<DssInternalSystemCommand> worker = ObjectUtil.checkNotNull(msg.getWorker(), "worker-master-actorRef");

        for (int i = 0; i < msg.getBossGroupCount(); i++) {
            final ActorRef<DssInternalSystemCommand> boss = getContext().spawn(DssBossEventActor.create(), "boss-" + i);
            bossSet.add(boss);
            boss.tell(DssInternalSystemCommand.BossInitialize
                    .builder()
                    .worker(worker)
                    .build());
        }

        return BehaviorImpl.same();
    }

    private Behavior<DssInternalSystemCommand> handlingServerListen(DssInternalSystemCommand.ServerListen msg) {
        if (bossSet.isEmpty()) {
            getLog().error("Cannot listen cause no active DssBossEventActor. try {}", listenErrorCount);
            if (listenErrorCount >= MAX_LISTEN_ERROR_COUNT) {
                getLog().error("There is no active DssBossEventActor. Terminate akka actor system");
                getContext().getSystem().terminate();
                return Behaviors.same();
            }

            getContext().scheduleOnce(Duration.ofSeconds(1), getSelf(), msg);
            listenErrorCount++;
            return Behaviors.same();
        }

        for (ActorRef<DssInternalSystemCommand> boss : bossSet) {
            boss.tell(msg);
        }

        return Behaviors.same();
    }
}
