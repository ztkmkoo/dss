package com.ztkmkoo.dss.server.rest.service;

import com.ztkmkoo.dss.core.actor.rest.DssRestActorService;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import com.ztkmkoo.dss.server.rest.entity.DssRestServiceRequestWrapper;
import com.ztkmkoo.dss.server.rest.entity.DssRestServiceResponseWrapper;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 2:03
 */
@Getter
public abstract class AbstractDssRestService implements DssRestActorService<DssRestServiceRequestWrapper, DssRestServiceResponseWrapper> {

    private final String name;
    private final String path;
    private final DssRestMethodType methodType;

    public <T extends AbstractDssRestService> AbstractDssRestService(Class<T> tClass, String path, DssRestMethodType methodType) {
        this(tClass.getSimpleName(), path, methodType);
    }

    public AbstractDssRestService(String name, String path, DssRestMethodType methodType) {
        this.name = name;
        this.path = path;
        this.methodType = methodType;
    }
}
