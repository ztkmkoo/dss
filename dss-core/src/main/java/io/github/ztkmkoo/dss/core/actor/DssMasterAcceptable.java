package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-11 22:28
 */
public interface DssMasterAcceptable {

    ActorRef<DssMasterCommand> getMasterActor();

    void setMasterActor(ActorRef<DssMasterCommand> actorRef);
}
