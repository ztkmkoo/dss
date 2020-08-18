package io.github.ztkmkoo.dss.core.actor.blocking;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

import java.util.Map;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-18 02:08
 */
public interface DssBlockingServiceResolver {

    Map<String, ActorRef<DssBlockingRestCommand>> getBlockingHttpClientServiceMap();

    ActorRef<DssBlockingRestCommand> getBlockingHttpClientServiceActor(String name);

    DssBlockingServiceResolverImpl addDssHttpClientService(String name, ActorRef<DssBlockingRestCommand> actorRef);
}
