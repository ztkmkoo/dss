package com.ztkmkoo.dss.core.network.rest.entity;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:54
 */
public class DssRestRequestTest {

    @Test
    public void builder() {

        final DssRestRequest request = DssRestRequest
                .builder()
                .methodType(DssRestMethodType.GET)
                .uri("/hi")
                .content("Hello")
                .build();

        assertNotNull(request);
        assertEquals(DssRestMethodType.GET, request.getMethodType());
        assertEquals("/hi", request.getUri());
        assertEquals("Hello", request.getContent());
    }
}