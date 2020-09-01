package io.github.ztkmkoo.dss.core.network.rest.entity;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:40
 */
@Getter
public class DssRestRequest implements Serializable {

    private static final long serialVersionUID = 8456589353995730809L;
    private final DssRestMethodType methodType;
    private final DssRestContentType contentType;
    private final String uri;
    private final String content;
    private final String charset;
    private final String boundary;

    @Builder
    protected DssRestRequest(DssRestMethodType methodType, String uri, DssRestContentType contentType, String content, String charset, String boundary) {
        this.methodType = methodType;
        this.contentType = contentType;
        this.uri = uri;
        this.content = content;
        this.charset = charset;
        this.boundary = boundary;
    }
}
