package io.github.ztkmkoo.dss.core.service;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.exception.service.DssActorServiceRuntimeException;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 03:45
 */
public abstract class AbstractDssService<R1 extends DssServiceCommand.DssServiceRequestCommand, S1 extends DssResolverCommand.DssServiceResponseCommand, R2 extends DssServiceRequest, S2 extends DssServiceResponse> implements DssService<R2, S2> {

    private final AtomicLong sequenceGenerator = new AtomicLong(0);
    private final Map<Long, R1> requestMap = new HashMap<>();

    @Getter(value = AccessLevel.PACKAGE)
    private final ActorRef<DssServiceCommand> serviceActor;

    public AbstractDssService(ActorRef<DssServiceCommand> serviceActor) {
        this.serviceActor = serviceActor;
    }

    public final void start(R1 request) {
        final long seq = sequenceGenerator.addAndGet(1);
        final R2 serviceRequest = makeRequest(request, seq);

        if (requestMap.containsKey(seq)) {
            throw new DssActorServiceRuntimeException();
        }

        requestMap.put(seq, request);

        start(serviceRequest);
    }

    @Override
    public final void end(S2 response) {
        final long seq = response.getSeq();
        if (!requestMap.containsKey(seq)) {
            throw new DssActorServiceRuntimeException();
        }

        final R1 request = requestMap.get(seq);
        request.getSender().tell(makeResponse(response, request.getChannelId()));
    }

    public abstract R2 makeRequest(R1 request, long seq);

    public abstract S1 makeResponse(S2 response, String channelId);
}
