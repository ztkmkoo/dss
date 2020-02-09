package com.ztkmkoo.dss.server.message;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:18
 */
public class HttpMessagesTest {

    @Test
    public void requestConstructor() {

        final HttpMessages.Request request = new HttpMessages.Request(
                "hi",
                "",
                null,
                Collections.emptyList(),
                null
        );

        assertNotNull(request);
        assertEquals("hi", request.getPath());
        assertTrue(request.getContent().isEmpty());
        assertNull(request.getHttpResponseHandlerActor());
        assertTrue(request.getFilterQueue().isEmpty());
        assertNull(request.getBusinessActor());

        final HttpMessages.Request request2 = new HttpMessages.Request(
                request,
                null,
                null
        );

        assertNotNull(request2);
        assertEquals("hi", request2.getPath());
        assertTrue(request2.getContent().isEmpty());
        assertNull(request2.getHttpResponseHandlerActor());
        assertTrue(request2.getFilterQueue().isEmpty());
        assertNull(request2.getBusinessActor());
    }
}