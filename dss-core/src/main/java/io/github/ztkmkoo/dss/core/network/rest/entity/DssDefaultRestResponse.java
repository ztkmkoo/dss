package io.github.ztkmkoo.dss.core.network.rest.entity;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 15:53
 */
public class DssDefaultRestResponse {

    @Getter
    private static final HttpResponse HTTP_NOT_FOUND = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);

    private DssDefaultRestResponse() {
    }
}
