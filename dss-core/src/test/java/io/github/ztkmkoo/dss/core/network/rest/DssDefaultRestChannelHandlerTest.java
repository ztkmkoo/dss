package io.github.ztkmkoo.dss.core.network.rest;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-15 23:15
 */
class DssDefaultRestChannelHandlerTest {

    @Mock
    ChannelHandlerContext ctx;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void channelRead0() throws Exception {
        final DssDefaultRestChannelHandler handler = new DssDefaultRestChannelHandler();

        final Field field = DssDefaultRestChannelHandler.class.getDeclaredField("buffer");
        field.setAccessible(true);
        final Object object = field.get(handler);
        if (! (object instanceof StringBuilder)) {
            fail();
        }

        final StringBuilder stringBuilder = (StringBuilder) object;
        assertNotNull(stringBuilder);

        final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "test");
        handler.channelRead0(ctx, request);
        assertEquals(0, stringBuilder.length());

        final LastHttpContent content = new DefaultLastHttpContent(Unpooled.copiedBuffer("Hello", CharsetUtil.UTF_8));
        handler.channelRead0(ctx, content);

        assertEquals("Hello", stringBuilder.toString());
    }
}