package io.github.ztkmkoo.dss.core.network.rest.property;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 1:02
 */
class DssRestChannelPropertyTest {

    @Test
    void builder() {

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder()
                .host("127.0.0.1")
                .port(8080)
                .build();

        assertNotNull(property);
        assertEquals("127.0.0.1", property.getHost());
        assertEquals(8080, property.getPort());
    }
}