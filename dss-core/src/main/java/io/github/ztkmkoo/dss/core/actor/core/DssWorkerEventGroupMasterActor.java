package io.github.ztkmkoo.dss.core.actor.core;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.message.core.DssInternalSystemCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-24 01:23
 */
public class DssWorkerEventGroupMasterActor extends AbstractDssCoreActor<DssInternalSystemCommand> implements DssEventGroupActor {

    public static Behavior<DssInternalSystemCommand> create() {
        return AbstractDssCoreActor.create(Behaviors.setup(DssWorkerEventGroupMasterActor::new));
    }

    private DssWorkerEventGroupMasterActor(ActorContext<DssInternalSystemCommand> context) {
        super(context);
    }

    @Override
    public Receive<DssInternalSystemCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(DssInternalSystemCommand.BossMasterInitialize.class, this::handlingBossMasterInitialize)
                .build();
    }

    private Behavior<DssInternalSystemCommand> handlingBossMasterInitialize(DssInternalSystemCommand.BossMasterInitialize msg) {
        return Behaviors.same();
    }
}
