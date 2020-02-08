package com.ztkmkoo.dss.network.http;

import com.ztkmkoo.dss.network.common.DssServerProperty;
import io.netty.bootstrap.ServerBootstrap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 2:08
 */
public class DssHttpServerImplTest {

    private final DssHttpServerImpl httpServer = new DssHttpServerImpl();

    @Test
    public void configServerBootstrap() throws InterruptedException {

        final DssServerProperty p = DssHttpServerProperty.builder(false).build();
        final ServerBootstrap b = new ServerBootstrap();

        final ServerBootstrap bootstrap = httpServer.configServerBootstrap(p, b);
        assertNotNull(bootstrap);
        assertEquals(b, bootstrap);
    }
}