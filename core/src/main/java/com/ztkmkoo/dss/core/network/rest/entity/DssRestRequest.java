package com.ztkmkoo.dss.core.network.rest.entity;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Builder;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:40
 */
@Getter
public class DssRestRequest {

    private final DssRestMethodType methodType;
    private final String uri;
    private final String content;

    @Builder
    private DssRestRequest(DssRestMethodType methodType, String uri, String content) {
        this.methodType = methodType;
        this.uri = uri;
        this.content = content;
    }
}
