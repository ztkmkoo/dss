package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingService;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 01:53
 */
public interface DssHttpClientService<R extends DssBlockingRestCommand.HttpRequest> extends DssBlockingService {

    void httpRequest(R request, ActorRef<DssBlockingRestCommand> restActor);
}
