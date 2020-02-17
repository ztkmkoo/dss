package com.ztkmkoo.dss.server.network.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 1:50
 */
public class AbstractDssHttpHandlerTest {

    @InjectMocks
    private AbstractDssHttpHandler handler= new DssHttpSimpleHandler(null);

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture cf;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito
                .when(ctx.writeAndFlush(any()))
                .thenReturn(cf);
    }

    @Test
    public void channelRead0() throws NoSuchFieldException, IllegalAccessException {

        final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hi");
        handler.channelRead0(ctx, request);

        final HttpContent content = new DefaultHttpContent(Unpooled.copiedBuffer("Hello", CharsetUtil.UTF_8));
        handler.channelRead0(ctx, content);

        final String bufferContent = getContent(handler);
        assertNotNull(bufferContent);
        assertEquals("Hello", bufferContent);
    }

    @Test
    public void channelReadComplete() {

        final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hi");
        handler.channelRead0(ctx, request);

        handler.channelReadComplete(ctx);

        assertNotNull(1);
    }

    @Test
    public void exceptionCaught() {

        handler.exceptionCaught(ctx, new Exception());
        assertNotNull(1);
    }

    private static String getContent(AbstractDssHttpHandler handler) throws NoSuchFieldException, IllegalAccessException {

        Objects.requireNonNull(handler);
        final Field field = AbstractDssHttpHandler.class.getDeclaredField("buffer");
        field.setAccessible(true);

        return field.get(handler).toString();
    }
}