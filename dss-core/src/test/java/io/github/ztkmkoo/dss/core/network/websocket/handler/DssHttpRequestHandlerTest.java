package io.github.ztkmkoo.dss.core.network.websocket.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class DssHttpRequestHandlerTest {

    private final String WEBSOCKET_PATH = "/websocket";

    private static <T> T getDssHttpRequestHandlerFieldWithReflection(DssHttpRequestHandler handler, String fieldName, Class<T> tcLass) throws NoSuchFieldException, IllegalAccessException {

        final Field field = DssHttpRequestHandler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return tcLass.cast(field.get(handler));
    }

    private static void mockChannelHandlerContextChannelId(ChannelHandlerContext ctx, String longId) {

        final Channel mockChannel = Mockito.mock(Channel.class);
        final ChannelId channelId = Mockito.mock(ChannelId.class);
        final ChannelPipeline pipeline = Mockito.mock(ChannelPipeline.class);
        final ChannelPromise promise = Mockito.mock(ChannelPromise.class);

        Mockito.when(ctx.channel()).thenReturn(mockChannel);
        Mockito.when(ctx.pipeline()).thenReturn(pipeline);
        Mockito.when(ctx.channel().pipeline()).thenReturn(pipeline);
        Mockito.when(ctx.channel().newPromise()).thenReturn(promise);
        Mockito.when(mockChannel.id()).thenReturn(channelId);
        Mockito.when(channelId.asLongText()).thenReturn(longId);
    }

    private final FullHttpRequest websocketRequest = requestWebsocket();
    private final ByteBuf content = Unpooled.copiedBuffer("Hello", CharsetUtil.UTF_8);
    private final FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/", content);

    @Mock
    private ChannelHandlerContext ctx;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void channelRead() throws Exception {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssHttpRequestHandler handler = new DssHttpRequestHandler(WEBSOCKET_PATH);

        handler.channelRead(ctx, httpRequest);

        final FullHttpRequest request = getDssHttpRequestHandlerFieldWithReflection(handler, "request", FullHttpRequest.class);
        assertEquals(HttpVersion.HTTP_1_1, request.protocolVersion());
        assertEquals(HttpMethod.GET, request.method());
        assertEquals("/", request.uri());
        assertEquals("Hello", request.content().toString(Charset.defaultCharset()));

        handler.channelRead(ctx, websocketRequest);

        final FullHttpRequest wsRequest = getDssHttpRequestHandlerFieldWithReflection(handler, "request", FullHttpRequest.class);
        assertEquals(HttpVersion.HTTP_1_1, wsRequest.protocolVersion());
        assertEquals(HttpMethod.GET, wsRequest.method());
        assertEquals("/websocket", wsRequest.uri());
        assertEquals("websocket", wsRequest.headers().get("Upgrade").toLowerCase());
        assertEquals("upgrade", wsRequest.headers().get("Connection").toLowerCase());
        assertEquals("dGhlIHNhbXBsZSBub25jZQ==", wsRequest.headers().get("Sec-WebSocket-Key"));
        assertEquals("http://test.com", wsRequest.headers().get("Sec-WebSocket-Origin"));
        assertEquals("13", wsRequest.headers().get("Sec-WebSocket-Version"));

        final WebSocketServerHandshaker handshaker = getDssHttpRequestHandlerFieldWithReflection(handler, "handshaker", WebSocketServerHandshaker.class);
        assertEquals("ws://dss.test.com/websocket", handshaker.uri());
        assertEquals("13", handshaker.version().toHttpHeaderValue());

    }

    @Test
    void channelReadComplete() throws Exception {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssHttpRequestHandler handler = new DssHttpRequestHandler(WEBSOCKET_PATH);

        handler.channelRead(ctx, websocketRequest);

        handler.channelReadComplete(ctx);

        Mockito.verify(ctx).flush();
    }

    @Test
    void exceptionCaught() {

        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssHttpRequestHandler handler = new DssHttpRequestHandler(WEBSOCKET_PATH);

        handler.exceptionCaught(ctx, new NullPointerException());

        assertTrue(true);
    }

    private FullHttpRequest requestWebsocket() {
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, WEBSOCKET_PATH, Unpooled.EMPTY_BUFFER);
        HttpHeaders headers = request.headers();

        headers.set(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET)
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE)
                .set(HttpHeaderNames.SEC_WEBSOCKET_KEY, "dGhlIHNhbXBsZSBub25jZQ==")
                .set(HttpHeaderNames.HOST, "dss.test.com")
                .set(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, "http://test.com")
                .set(HttpHeaderNames.SEC_WEBSOCKET_VERSION, "13");

        return request;
    }
}
