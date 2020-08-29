package io.github.ztkmkoo.dss.core.actor.rest.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 5. 오후 6:44
 */
public class DssRestServiceRequestDefaultImpl<S extends Serializable> implements DssRestServiceRequest<S> {
    private static final long serialVersionUID = 7455899122399351126L;

    @Getter
    private final S body;

    @Builder
    private DssRestServiceRequestDefaultImpl(S body) {
        this.body = body;
    }
}
