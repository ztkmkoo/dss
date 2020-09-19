package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 02:57
 */
public interface DssServiceActorResolvable<K> {

    K getKey();

    ActorRef<DssServiceCommand> getActorRef();
}
