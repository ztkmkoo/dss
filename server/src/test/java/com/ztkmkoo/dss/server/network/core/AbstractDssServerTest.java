package com.ztkmkoo.dss.server.network.core;

import com.ztkmkoo.dss.server.network.http.DssHttpServerProperty;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
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