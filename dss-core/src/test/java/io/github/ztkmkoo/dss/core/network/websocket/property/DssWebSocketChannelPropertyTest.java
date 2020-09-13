package io.github.ztkmkoo.dss.core.network.websocket.property;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DssWebSocketChannelPropertyTest {

    @Test
    void builder() {

        final DssWebSocketChannelProperty property = DssWebSocketChannelProperty
                .builder()
                .host("127.0.0.1")
                .port(8080)
                .build();

        assertNotNull(property);
        assertEquals("127.0.0.1", property.getHost());
        assertEquals(8080, property.getPort());
    }
}
