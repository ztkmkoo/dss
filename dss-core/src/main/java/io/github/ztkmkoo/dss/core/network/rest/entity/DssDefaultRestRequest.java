package io.github.ztkmkoo.dss.core.network.rest.entity;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 16:12
 */
public class DssDefaultRestRequest implements DssRestRequest {
    private static final long serialVersionUID = -7882898488438188030L;

    private final transient HttpRequest httpRequest;

    public DssDefaultRestRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public String getPath() {
        return httpRequest.uri();
    }

    @Override
    public DssRestMethodType getMethodType() {
        return null;
    }

    @Override
    public DssRestContentType getRestContentType() {
        return null;
    }
}
