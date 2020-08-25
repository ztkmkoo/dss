package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-22 23:51
 */
public abstract class AbstractDssActor<T extends DssCommand> extends AbstractBehavior<T> implements DssActor {

    protected AbstractDssActor(ActorContext<T> context) {
        super(context);
    }

    protected ActorRef<T> getSelf() {
        return getContext().getSelf();
    }
}
