package io.github.ztkmkoo.dss.core.network.rest.entity;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 16:12
 */
public class DssDefaultRestRequest implements DssRestRequest {
    private static final long serialVersionUID = -7882898488438188030L;

    private static final DssRestContentType DEFAULT_CONTENT_TYPE = DssRestContentType.APPLICATION_JSON;

    private final transient HttpRequest httpRequest;

    @Getter
    private final DssRestMethodType methodType;
    @Getter
    private final DssRestContentType restContentType;

    public DssDefaultRestRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.methodType = DssRestMethodType.fromNettyHttpMethod(httpRequest.method());

        final String contentType = httpRequest.headers().get("content-Type");
        if (StringUtils.nonEmpty(contentType)) {
            this.restContentType = DssRestContentType.fromText(contentType);
        } else {
            this.restContentType = DEFAULT_CONTENT_TYPE;
        }
    }

    @Override
    public String getPath() {
        return httpRequest.uri();
    }
}
