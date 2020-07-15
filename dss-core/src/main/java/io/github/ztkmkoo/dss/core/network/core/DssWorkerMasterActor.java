package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssBehavior;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 04:30
 */
public class DssWorkerMasterActor extends AbstractDssBehavior<InternalNettyCommand> {

    public static Behavior<InternalNettyCommand> create(int workerThreadCount) {
        return Behaviors.setup(context -> new DssWorkerMasterActor(context, workerThreadCount));
    }

    private final int workerThreadCount;

    private DssWorkerMasterActor(ActorContext<InternalNettyCommand> context, int workerThreadCount) {
        super(context);
        this.workerThreadCount = workerThreadCount;
    }

    @Override
    public Receive<InternalNettyCommand> createReceive() {
        return null;
    }
}
