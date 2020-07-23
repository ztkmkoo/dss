package io.github.ztkmkoo.dss.core.actor.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-22 23:17
 */
abstract class AbstractDssCoreActor<T extends DssCommand> extends AbstractBehavior<T> {

    protected static <T extends DssCommand> Behavior<T> create(Behavior<T> behavior) {
        return Behaviors.logMessages(
                Behaviors
                        .supervise(behavior)
                        .onFailure(SupervisorStrategy.restart())
        );
    }

    protected AbstractDssCoreActor(ActorContext<T> context) {
        super(context);

        getLog().info("Initialize {}", context.getSelf().path());
    }

    protected Logger getLog() {
        return getContext().getLog();
    }

    protected ActorRef<T> getSelf() {
        return getContext().getSelf();
    }

    protected <T> void requireNonNullThenTerminate(T nonNullValue, String type) {
        if (Objects.isNull(nonNullValue)) {
            getLog().error("{} is null then terminate akka actor system.", type);
            getContext().getSystem().terminate();
        }
    }
}
