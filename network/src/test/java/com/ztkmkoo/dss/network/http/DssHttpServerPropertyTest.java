package com.ztkmkoo.dss.network.http;

import com.ztkmkoo.dss.network.enumeration.DssNetworkType;
import io.netty.channel.ChannelHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public class DssHttpServerPropertyTest {

    @Test
    public void build() {

        final DssHttpServerProperty property = DssHttpServerProperty
                .builder(false)
                .host("127.0.0.1")
                .port(8181)
                .bossThread(1)
                .workerThread(16)
                .handlerCreator(DssHttpSimpleHandler::new)
                .build();

        assertNotNull(property);
        assertEquals(DssNetworkType.HTTP, property.getNetworkType());
        assertEquals(false, property.isSsl());
        assertEquals("127.0.0.1", property.getHost());
        assertEquals(8181, property.getPort());
        assertEquals(1, property.getBossThread());
        assertEquals(16, property.getWorkerThread());

        final ChannelHandler channelHandler = property.getHandlerCreator().createChannelHandler();
        assertNotNull(channelHandler);
        assertEquals(DssHttpSimpleHandler.class, channelHandler.getClass());
    }

    @Test
    public void buildSsl() {

        final DssHttpServerProperty property = DssHttpServerProperty.builder(true).build();

        assertNotNull(property);
        assertEquals(DssNetworkType.HTTP, property.getNetworkType());
        assertEquals(true, property.isSsl());
        assertEquals(8443, property.getPort());
    }

    @Test
    public void buildWithoutAnyParameters() {

        final DssHttpServerProperty property = DssHttpServerProperty.builder(false).build();

        assertNotNull(property);
        assertEquals(DssNetworkType.HTTP, property.getNetworkType());
        assertEquals(false, property.isSsl());
        assertEquals("0.0.0.0", property.getHost());
        assertEquals(8080, property.getPort());
        assertEquals(0, property.getBossThread());
        assertEquals(0, property.getWorkerThread());
    }
}