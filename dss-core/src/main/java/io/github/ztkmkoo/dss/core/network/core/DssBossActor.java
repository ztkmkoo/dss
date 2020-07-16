package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssBehavior;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 22:40
 */
public class DssBossActor extends AbstractDssBehavior<InternalNettyCommand> implements DssSingleThreadEventLoop {

    public static Behavior<InternalNettyCommand> create(ActorRef<InternalNettyCommand> workerMaster) {
        return Behaviors.setup(context -> new DssBossActor(context, workerMaster));
    }

    private final ActorRef<InternalNettyCommand> workerMaster;

    private DssBossActor(ActorContext<InternalNettyCommand> context, ActorRef<InternalNettyCommand> workerMaster) {
        super(context);
        this.workerMaster = workerMaster;
    }

    @Override
    public Receive<InternalNettyCommand> createReceive() {
        return newReceiveBuilder()
                .build();
    }
}
