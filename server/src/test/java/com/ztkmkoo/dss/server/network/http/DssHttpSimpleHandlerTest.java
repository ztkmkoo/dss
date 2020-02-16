package com.ztkmkoo.dss.server.network.http;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.actor.http.HttpMasterActor;
import com.ztkmkoo.dss.server.message.HttpMessages;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.After;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 1:26
 */
public class DssHttpSimpleHandlerTest {

    private static final ActorTestKit testKit = ActorTestKit.create();

    @After
    public void cleanup() {
        testKit.shutdownTestKit();
    }

    @InjectMocks
    private DssHttpSimpleHandler handler;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ChannelFuture cf;

    private static final HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hi");
    private static final String CONTENT = "Hello World";

    @Test
    public void handlingHttpRequest() {

        final ActorRef<HttpMessages.Request> masterActor = testKit.spawn(HttpMasterActor.create());
        this.handler = new DssHttpSimpleHandler(masterActor);

        handler.handlingHttpRequest(ctx, request, CONTENT);
        assertNotNull(true);
    }

    @Test
    public void handlingHttpRequest2() {

        MockitoAnnotations.initMocks(this);

        Mockito
                .when(ctx.writeAndFlush(any()))
                .thenReturn(cf);

        handler.handlingHttpRequest(ctx, request);
        assertNotNull(true);
    }
}