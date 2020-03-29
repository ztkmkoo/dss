package com.ztkmkoo.dss.core.actor.rest.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 29. 오전 5:53
 */
@Getter
public class DssRestServiceRequestJsonImpl<S extends Serializable> implements DssRestServiceRequest<S> {

    private static final long serialVersionUID = 6193411823980317309L;
    private final S body;

    @Builder
    protected DssRestServiceRequestJsonImpl(S body) {
        this.body = body;
    }
}
