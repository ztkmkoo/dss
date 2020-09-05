package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-24 02:28
 */
public interface DssResolverActor extends DssActor<DssResolverCommand> {

    ActorRef<DssMasterCommand> getMasterActor();
}
