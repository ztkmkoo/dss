package io.github.ztkmkoo.dss.core.actor.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.message.core.DssInternalSystemCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-22 23:09
 */
public final class DssRootActor extends AbstractDssCoreActor<DssInternalSystemCommand> {

    public static Behavior<DssInternalSystemCommand> create() {
        return AbstractDssCoreActor.create(Behaviors.setup(DssRootActor::new));
    }

    private ActorRef<DssInternalSystemCommand> boss;

    private DssRootActor(ActorContext<DssInternalSystemCommand> context) {
        super(context);
    }

    @Override
    public Receive<DssInternalSystemCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(DssInternalSystemCommand.RootInitialize.class, this::handlingRootInitialize)
                .onMessage(DssInternalSystemCommand.ServerListen.class, this::handlingServerListen)
                .build();
    }

    private Behavior<DssInternalSystemCommand> handlingRootInitialize(DssInternalSystemCommand.RootInitialize msg) {
        boss = getContext().spawn(DssBossEventGroupMasterActor.create(), "boss-master");
        final ActorRef<DssInternalSystemCommand> worker = getContext().spawn(DssWorkerEventGroupMasterActor.create(), "worker-master");

        boss.tell(DssInternalSystemCommand.BossMasterInitialize
                .builder()
                .bossGroupCount(msg.getBossGroupCount())
                .worker(worker)
                .build());

        return Behaviors.same();
    }

    private Behavior<DssInternalSystemCommand> handlingServerListen(DssInternalSystemCommand.ServerListen msg) {
        requireNonNullThenTerminate(boss, "bossActorRef");
        boss.tell(msg);
        return Behaviors.same();
    }
}
