package io.github.ztkmkoo.dss.core.service;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 01:22
 */
public interface DssServiceBuilder<R1 extends DssServiceCommand.DssServiceRequestCommand, S1 extends DssResolverCommand.DssServiceResponseCommand, R2 extends DssServiceRequest, S2 extends DssServiceResponse> {

    /**
     * DssService create
     */
    AbstractDssService<R1, S1, R2, S2> create(ActorRef<DssServiceCommand> serviceActor);
}
