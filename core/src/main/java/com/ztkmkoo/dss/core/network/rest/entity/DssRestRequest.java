package com.ztkmkoo.dss.core.network.rest.entity;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
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
    private final String uri;
    private final String content;

    @Builder
    protected DssRestRequest(DssRestMethodType methodType, String uri, String content) {
        this.methodType = methodType;
        this.uri = uri;
        this.content = content;
    }
}
