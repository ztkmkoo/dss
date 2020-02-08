package com.ztkmkoo.dss.network.http;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 1:26
 */
public class DssHttpSimpleHandlerTest {

    @InjectMocks
    private DssHttpSimpleHandler handler;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture cf;

    private final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hi");
    private final String content = "Hello World";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito
                .when(ctx.writeAndFlush(Mockito.anyObject()))
                .thenReturn(cf);
    }

    @Test
    public void handlingHttpRequest() {

        handler.handlingHttpRequest(ctx, request, content);
        assertNotNull(true);
    }

    @Test
    public void handlingHttpRequest2() {

        handler.handlingHttpRequest(ctx, request);
        assertNotNull(true);
    }
}