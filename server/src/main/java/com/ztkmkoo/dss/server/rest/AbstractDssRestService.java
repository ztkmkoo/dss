package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.actor.rest.DssRestActorService;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 2:03
 */
@Getter
public abstract class AbstractDssRestService implements DssRestActorService {

    private final String uri;
    private final DssRestMethodType methodType;

    public AbstractDssRestService(String uri, DssRestMethodType methodType) {
        this.uri = uri;
        this.methodType = methodType;
    }

    public abstract Object handling(Object request);
}
