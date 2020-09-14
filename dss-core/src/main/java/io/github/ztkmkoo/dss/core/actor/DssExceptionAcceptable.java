package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssExceptionCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 01:48
 */
public interface DssExceptionAcceptable {

    ActorRef<DssExceptionCommand> getExceptionActor();

    void setExceptionActor(ActorRef<DssExceptionCommand> actorRef);
}
