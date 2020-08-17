package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingService;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 01:53
 */
public interface DssHttpClientService<R extends DssBlockingRestCommand.HttpRequest> extends DssBlockingService {

    /**
     * Set the name of this service.
     * You could find this service actor ref from this value
     * @return service actor name
     */
    String getName();

    /**
     * Handling http request
     * @param request
     * @param restActor
     */
    void httpRequest(R request, ActorRef<DssBlockingRestCommand> restActor);
}
