package com.ztkmkoo.dss.server.network.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestRequest;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestResponse;
import com.ztkmkoo.dss.server.network.rest.enumeration.DssRestMethod;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 27. 오전 1:09
 */
public class HttpEntityConvertUtils {

    private HttpEntityConvertUtils() {}

    /**
     * Convert netty http request to DssRestRequest
     * @param request {@see io.netty.handler.codec.http.HttpRequest}
     * @param content request body json string content
     * @return {@see com.ztkmkoo.dss.server.network.rest.entity.DssRestRequest}
     */
    public static DssRestRequest getDssRestRequestFromNettyHttpRequest(HttpRequest request, String content) {

        Objects.requireNonNull(request);
        Objects.requireNonNull(content);

        return DssRestRequest
                .builder()
                .uri(request.uri())
                .method(DssRestMethod.fromNettyHttpMethod(request.method()))
                .content(content)
                .build();
    }

    /**
     * Convert DssRestResponse to netty http response
     * @param restResponse {@see com.ztkmkoo.dss.server.network.rest.entity.DssRestResponse}
     * @return {@see io.netty.handler.codec.http.HttpResponse}
     */
    public static HttpResponse getHttpResponseFromDssRestResponse(DssRestResponse restResponse) {

        Objects.requireNonNull(restResponse);

        try {
            final ByteBuf responseContent = responseContent(restResponse);
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    responseContent
            );
        } catch (Exception e) {
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    Unpooled.EMPTY_BUFFER
            );
        }
    }

    private static ByteBuf responseContent(DssRestResponse restResponse) throws JsonProcessingException {

        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(restResponse);
        return Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
    }
}
