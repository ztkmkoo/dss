package io.github.ztkmkoo.dss.core.actor.rest.service;

import io.github.ztkmkoo.dss.core.actor.AbstractDssActorService;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequestDefaultImpl;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 5. 오후 9:27
 */
@Getter
public abstract class AbstractDssRestActorService<S extends Serializable>
        extends AbstractDssActorService<DssRestServiceActorCommand, DssRestServiceActorCommandRequest>
        implements DssRestActorService {

    protected final String name;
    protected final String path;
    protected final DssRestMethodType methodType;
    protected final DssRestContentInfo consume;
    protected final DssRestContentInfo produce;



    public AbstractDssRestActorService(
            String name,
            String path,
            DssRestMethodType methodType,
            DssRestContentInfo consume,
            DssRestContentInfo produce) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(path);
        Objects.requireNonNull(methodType);

        this.name = name;
        this.path = path;
        this.methodType = methodType;
        this.consume = consume;
        this.produce = produce;
    }

    public AbstractDssRestActorService(
            String name,
            String path,
            DssRestMethodType methodType) {
        this(name, path, methodType, DssRestContentInfo.APPLICATION_JSON_UTF8, DssRestContentInfo.APPLICATION_JSON_UTF8);
    }

    protected abstract DssRestServiceResponse handlingRequest(DssRestServiceRequest<S> request);
    protected abstract S getBody(String content);

    @Override
    public final DssRestServiceResponse handling(DssRestServiceActorCommandRequest commandRequest) {
        final DssRestServiceRequest<S> req = makeRequest(commandRequest);
        return handlingRequest(req);
    }

    @SuppressWarnings("unchecked")
    private DssRestServiceRequest<S> makeRequest(DssRestServiceActorCommandRequest commandRequest) {
        Objects.requireNonNull(commandRequest);
        final S body = getBody(commandRequest.getContent());
        return (DssRestServiceRequest<S>) DssRestServiceRequestDefaultImpl
                .builder()
                .body(body)
                .build();
    }

    @Override
    public final void onReceive(DssRestServiceActorCommandRequest req) {
        final DssRestServiceRequest<S> r = makeRequest(req);
        onReceive(r);
    }

    protected void onReceive(DssRestServiceRequest<S> request) {
        // do nothing
        // override this if need
    }
}
