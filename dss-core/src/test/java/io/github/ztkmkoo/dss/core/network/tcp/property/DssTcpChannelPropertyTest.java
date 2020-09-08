package io.github.ztkmkoo.dss.core.network.tcp.property;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;

class DssTcpChannelPropertyTest {
    @Test
    public void builder() {

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