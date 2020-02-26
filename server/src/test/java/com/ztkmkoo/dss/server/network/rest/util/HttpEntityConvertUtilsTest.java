package com.ztkmkoo.dss.server.network.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestRequest;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestResponse;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestSuccessResponse;
import com.ztkmkoo.dss.server.network.rest.enumeration.DssRestMethod;
import com.ztkmkoo.dss.server.util.JacksonUtils;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 27. 오전 1:27
 */
public class HttpEntityConvertUtilsTest {

    @Test
    @SuppressWarnings("unchecked")
    public void getDssRestRequestFromNettyHttpRequest() throws JsonProcessingException {

        final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/test/hi");
        final String content = "{\"name\":\"kebron\"}";
        final DssRestRequest dssRestRequest = HttpEntityConvertUtils.getDssRestRequestFromNettyHttpRequest(request, content);

        assertEquals(DssRestMethod.GET, dssRestRequest.getMethod());
        assertEquals("/test/hi", dssRestRequest.getUri());
        assertEquals(content, dssRestRequest.getContent());

        final HashMap<String, String> jsonMap = dssRestRequest.getContentByTypedReference(new TypeReference<HashMap>() {});
        assertEquals("kebron", jsonMap.get("name"));
    }

    @Test(expected = NullPointerException.class)
    public void getDssRestRequestFromNettyHttpRequestWithNullParams() {
        HttpEntityConvertUtils.getDssRestRequestFromNettyHttpRequest(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void getDssRestRequestFromNettyHttpRequestWithNullParams2() {
        final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/test/hi");
        HttpEntityConvertUtils.getDssRestRequestFromNettyHttpRequest(request, null);
    }

    @Test
    public void getHttpResponseFromDssRestResponse() throws InvalidObjectException, JsonProcessingException {

        final DssRestResponse dssRestResponse = new DssRestSuccessResponse(0, "tired..");
        final HttpResponse httpResponse = HttpEntityConvertUtils.getHttpResponseFromDssRestResponse(dssRestResponse);

        assertNotNull(httpResponse);
        assertEquals(HttpResponseStatus.OK, httpResponse.status());

        if (httpResponse instanceof DefaultFullHttpResponse) {
            final DefaultFullHttpResponse defaultFullHttpResponse = (DefaultFullHttpResponse) httpResponse;
            final String content = defaultFullHttpResponse.content().toString(CharsetUtil.UTF_8);
            final HashMap<String, Object> jsonMap = JacksonUtils.readJsonString(content, new TypeReference<HashMap<String, Object>>() {});

            assertEquals(0, jsonMap.get("status"));
            assertEquals("tired..", jsonMap.get("message"));
        } else {
            throw new InvalidObjectException("Not expected class: " + httpResponse.getClass().getName());
        }
    }

    @Test(expected = NullPointerException.class)
    public void getHttpResponseFromDssRestResponseWithNullParams() {
        HttpEntityConvertUtils.getHttpResponseFromDssRestResponse(null);
    }
}