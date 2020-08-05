package io.github.ztkmkoo.dss.core.network;

import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-01 21:45
 */
public interface DssActorAcceptable<C extends DssCommand> {
    Behavior<C> create();
}
