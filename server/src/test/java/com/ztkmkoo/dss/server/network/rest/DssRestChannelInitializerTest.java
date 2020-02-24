package com.ztkmkoo.dss.server.network.rest;

import com.ztkmkoo.dss.server.network.rest.handler.DssRestChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 12:55
 */
public class DssRestChannelInitializerTest {

    @Mock
    private ChannelPipeline channelPipeline;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void defaultConstructor() throws NoSuchFieldException, IllegalAccessException {

        final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(null);
        assertNotNull(channelInitializer);

        final long timeout = getDssRestChannelInitializerReflectionFieldValue("timeout", channelInitializer, Long.class);
        final TimeUnit timeUnit = getDssRestChannelInitializerReflectionFieldValue("timeUnit", channelInitializer, TimeUnit.class);

        assertEquals(3000, timeout);
        assertEquals(TimeUnit.MILLISECONDS, timeUnit);
    }

    @Test
    public void initChannelPipeline() {

        final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(null);
        channelInitializer.initChannelPipeline(channelPipeline);
        assertTrue(true);
    }

    @Test
    public void initSslChannelPipeline() {

        final DssRestChannelInitializer channelInitializer = new DssRestChannelInitializer(null);
        channelInitializer.initSslChannelPipeline(channelPipeline);
        assertTrue(true);
    }

    private <T> T getDssRestChannelInitializerReflectionFieldValue(String name, Object o, Class<T> tClass) throws NoSuchFieldException, IllegalAccessException {

        final Field field = DssRestChannelInitializer.class.getDeclaredField(name);
        Objects.requireNonNull(field);
        field.setAccessible(true);
        return tClass.cast(field.get(o));
    }
}