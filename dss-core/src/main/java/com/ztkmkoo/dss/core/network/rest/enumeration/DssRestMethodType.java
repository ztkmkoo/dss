package com.ztkmkoo.dss.core.network.rest.enumeration;

import com.ztkmkoo.dss.core.network.rest.exception.NotSupportedRestMethodException;
import io.netty.handler.codec.http.HttpMethod;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:44
 */
public enum DssRestMethodType {

    POST(HttpMethod.POST),
    GET(HttpMethod.GET),
    PUT(HttpMethod.PUT),
    DELETE(HttpMethod.DELETE),
    ;

    private static final Map<HttpMethod, DssRestMethodType> nettyHttpMethodMapper = initNettyHttpMethodMapper();
    private final HttpMethod method;

    DssRestMethodType(HttpMethod method) {
        this.method = method;
    }

    public static DssRestMethodType fromNettyHttpMethod(HttpMethod method) {

        Objects.requireNonNull(method);
        if (!nettyHttpMethodMapper.containsKey(method)) {
            throw new NotSupportedRestMethodException("Not support " + method.name());
        }
        return nettyHttpMethodMapper.get(method);
    }

    private static Map<HttpMethod, DssRestMethodType> initNettyHttpMethodMapper() {

        final DssRestMethodType[] methods = values();
        if (methods.length == 0) {
            return Collections.unmodifiableMap(Collections.emptyMap());
        }

        return Collections
                .unmodifiableMap(
                        Stream
                                .of(methods)
                                .collect(
                                        Collectors.toMap(
                                                o -> o.method,
                                                o -> o
                                        )
                                )
                );
    }
}
