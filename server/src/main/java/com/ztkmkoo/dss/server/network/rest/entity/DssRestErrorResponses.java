package com.ztkmkoo.dss.server.network.rest.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 20. 오전 12:02
 */
public class DssRestErrorResponses {

    private static final Logger logger = LoggerFactory.getLogger(DssRestErrorResponses.class);

    public static final HttpResponse REQUEST_TIMEOUT = restResponse(HttpResponseStatus.REQUEST_TIMEOUT, "Request timeout.");

    private DssRestErrorResponses() {}

    private static HttpResponse restResponse(HttpResponseStatus status, String message) {

        final DssRestErrorResponse errorResponse = new DssRestErrorResponse(status.code(), message);
        final String content = errorResponseAsJsonString(errorResponse);

        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Objects.isNull(content) ?
                        Unpooled.EMPTY_BUFFER :
                        Unpooled.copiedBuffer(content, CharsetUtil.UTF_8)
        );
    }

    private static String errorResponseAsJsonString(DssRestErrorResponse errorResponse) {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(errorResponse);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException: ", e);
            return null;
        }
    }
}
