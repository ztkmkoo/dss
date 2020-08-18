package io.github.ztkmkoo.dss.core.actor.blocking;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-18 02:08
 */
public interface DssBlockingServiceResolver {

    ActorRef<DssBlockingRestCommand> getBlockingHttpClientServiceActor(String name);

    DssBlockingServiceResolverImpl addDssHttpClientService(String name, ActorRef<DssBlockingRestCommand> actorRef);
}
