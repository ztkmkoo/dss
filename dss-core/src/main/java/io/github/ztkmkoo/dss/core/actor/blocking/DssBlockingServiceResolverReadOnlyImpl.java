package io.github.ztkmkoo.dss.core.actor.blocking;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-18 22:49
 */
public class DssBlockingServiceResolverReadOnlyImpl extends DssBlockingServiceResolverImpl {

    public DssBlockingServiceResolverReadOnlyImpl(DssBlockingServiceResolver resolver) {
        super(resolver);
    }

    @Override
    public DssBlockingServiceResolverImpl addDssHttpClientService(String name, ActorRef<DssBlockingRestCommand> actorRef) {
        // do nothing
        return this;
    }
}
