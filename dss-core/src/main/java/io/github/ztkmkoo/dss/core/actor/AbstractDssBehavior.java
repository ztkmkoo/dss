package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 04:44
 */
public abstract class AbstractDssBehavior<T extends Serializable> extends AbstractBehavior<T> {
    private final String name;

    protected AbstractDssBehavior(ActorContext<T> context) {
        super(context);
        this.name = String.format("%s[%s]", getClass().getSimpleName(), getSelf().path().name());
        getLog().debug("{} Initialized.", name);
    }

    protected Logger getLog() {
        return getContext().getLog();
    }

    protected ActorRef<T> getSelf() {
        return getContext().getSelf();
    }

    protected void logMessage(Serializable msg) {
        getLog().debug("{} receive: {}", name, msg);
    }
}
