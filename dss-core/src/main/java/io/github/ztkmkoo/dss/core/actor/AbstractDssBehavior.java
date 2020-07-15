package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 04:44
 */
public abstract class AbstractDssBehavior<T extends Serializable> extends AbstractBehavior<T> {
    protected AbstractDssBehavior(ActorContext<T> context) {
        super(context);
        getLog().debug("{} Initialized.", getClass().getSimpleName());
    }

    protected Logger getLog() {
        return getContext().getLog();
    }
}
