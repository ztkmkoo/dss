package io.github.ztkmkoo.dss.core.network.rest.entity;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                .charset("UTF-8")
                .contentType(DssRestContentType.APPLICATION_JSON)
                .build();

        assertNotNull(request);
        assertEquals(DssRestMethodType.GET, request.getMethodType());
        assertEquals("/hi", request.getUri());
        assertEquals("Hello", request.getContent());
        assertEquals(DssRestContentType.APPLICATION_JSON, request.getContentType());
        assertEquals("UTF-8", request.getCharset());
    }
}