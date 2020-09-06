package io.github.ztkmkoo.dss.core.network.rest.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:54
 */
class DssRestRequestTest {

    @Test
    void builder() {

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