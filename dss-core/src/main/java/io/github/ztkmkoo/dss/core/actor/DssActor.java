package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import org.slf4j.Logger;

/**
 * Root interface of all dss actors
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-22 23:55
 */
public interface DssActor<T extends DssCommand> {

    /**
     * Get self behavior
     */
    Behavior<T> getBehavior();

    /**
     * Get logger
     */
    Logger getLog();
}
