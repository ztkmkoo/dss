package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 02:45
 */
public interface DssNetworkAcceptable {

    ActorRef<DssNetworkCommand> getNetworkActor();

    void setNetworkActor(ActorRef<DssNetworkCommand> actorRef);
}
