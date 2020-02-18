package com.ztkmkoo.dss.server.network.rest;

import com.ztkmkoo.dss.server.network.core.enumeration.NettyLogLevelWrapperType;
import com.ztkmkoo.dss.server.network.core.handler.DssChannelInitializer;
import com.ztkmkoo.dss.server.network.core.service.DssServiceCreator;
import io.netty.channel.socket.SocketChannel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 18. 오후 11:56
 */
public class DssRestChannelPropertyTest {

    private final Logger logger = LoggerFactory.getLogger(DssRestChannelPropertyTest.class);

    @Mock
    private DssChannelInitializer<SocketChannel> dssChannelInitializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEmpty() {

        logger.info("testEmpty starts");

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder(dssChannelInitializer)
                .build();

        assertNotNull(property);
        assertEquals("0.0.0.0", property.getHost());
        assertEquals(8080, property.getPort());
        assertEquals(0, property.getBossThread());
        assertEquals(0, property.getWorkerThread());
        assertNotNull(property.getDssChannelInitializer());
        assertTrue(property.getChannelServiceCreatorList().isEmpty());
        assertEquals(NettyLogLevelWrapperType.INFO, property.getLogLevel());
    }

    @Test
    public void testBuild() {

        logger.info("testBuild starts");

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder(dssChannelInitializer)
                .host("127.0.0.1")
                .port(8181)
                .bossThread(16)
                .workerThread(16)
                .channelServiceCreatorList(Collections.emptyList())
                .logLevel(NettyLogLevelWrapperType.DEBUG)
                .build();

        assertNotNull(property);
        assertEquals("127.0.0.1", property.getHost());
        assertEquals(8181, property.getPort());
        assertEquals(16, property.getBossThread());
        assertEquals(16, property.getWorkerThread());
        assertNotNull(property.getDssChannelInitializer());
        assertTrue(property.getChannelServiceCreatorList().isEmpty());
        assertEquals(NettyLogLevelWrapperType.DEBUG, property.getLogLevel());
    }

    @Test
    public void testAddChannelServiceCreator() {

        logger.info("testAddChannelServiceCreator starts");

        final DssServiceCreator creator = Mockito.mock(DssServiceCreator.class);

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder(dssChannelInitializer)
                .addChannelServiceCreatorList(creator)
                .build();

        assertEquals(creator, property.getChannelServiceCreatorList().get(0));
    }
}