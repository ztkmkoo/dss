package com.ztkmkoo.dss.network.common;

import com.ztkmkoo.dss.network.http.DssHttpServerProperty;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AbstractDssServerTest {

    private final DssServerProperty property = DssHttpServerProperty
            .builder(false)
            .build();

    @Test
    public void bind() throws InterruptedException {

        final DssServer dssServer = property.getNetworkType().getCreator().create();
        assertNotNull(dssServer);

        dssServer.bind(property);
    }
}