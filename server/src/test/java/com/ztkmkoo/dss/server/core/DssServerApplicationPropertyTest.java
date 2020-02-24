package com.ztkmkoo.dss.server.core;

import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.network.http.DssHttpServerProperty;
import com.ztkmkoo.dss.server.network.rest.DssRestChannelProperty;
import com.ztkmkoo.dss.server.network.rest.handler.DssRestChannelInitializer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 2:22
 */
public class DssServerApplicationPropertyTest {

    @Test
    public void builder() {
        final DssServerApplicationProperty.Builder builder = DssServerApplicationProperty.builder(DssNetworkType.REST);
        assertNotNull(builder);
    }

    @Test
    public void getNetworkPropertyWithoutServerProperty() {
        final DssServerApplicationProperty property = DssServerApplicationProperty
                .builder(DssNetworkType.REST).build();
        assertNotNull(property);
        assertNull(property.getNetworkProperty());
    }

    @Test
    public void getNetworkProperty() {
        final DssServerApplicationProperty property = DssServerApplicationProperty
                .builder(DssNetworkType.REST)
                .networkProperty(DssRestChannelProperty
                        .builder(DssRestChannelInitializer::new)
                        .build()
                ).build();
        assertNotNull(property);
        assertNotNull(property.getNetworkProperty());
        assertEquals(DssHttpServerProperty.class, property.getNetworkProperty().getClass());
    }
}