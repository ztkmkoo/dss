package io.github.ztkmkoo.dss.core.network.websocket.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;

class DssWebSocketHandlerTest {

    private static <T> T getDssWebSocketHandlerFieldWithReflection(DssWebSocketHandler handler, String fieldName, Class<T> tcLass) throws NoSuchFieldException, IllegalAccessException {

        final Field field = DssWebSocketHandler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return tcLass.cast(field.get(handler));
    }

    private static void mockChannelHandlerContextChannelId(ChannelHandlerContext ctx, String longId) {

        final Channel mockChannel = Mockito.mock(Channel.class);
        final ChannelId channelId = Mockito.mock(ChannelId.class);

        Mockito.when(ctx.channel()).thenReturn(mockChannel);
        Mockito.when(mockChannel.id()).thenReturn(channelId);
        Mockito.when(channelId.asLongText()).thenReturn(longId);
    }

    private final TextWebSocketFrame message = new TextWebSocketFrame("Hello");
    private final CloseWebSocketFrame closeMessage = new CloseWebSocketFrame(WebSocketCloseStatus.NORMAL_CLOSURE);

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private Channel channel;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void channelRead() throws Exception {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssWebSocketHandler handler = new DssWebSocketHandler();

        handler.channelRead(ctx, message);

        final TextWebSocketFrame msg = getDssWebSocketHandlerFieldWithReflection(handler, "message", TextWebSocketFrame.class);
        assertEquals("Hello", msg.text());

        handler.channelRead(ctx, closeMessage);

        final CloseWebSocketFrame closeMsg = getDssWebSocketHandlerFieldWithReflection(handler, "closeMessage", CloseWebSocketFrame.class);
        assertEquals("Bye", closeMsg.reasonText());
        assertEquals(1000, closeMsg.statusCode());
    }

    @Test
    void channelReadComplete() {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssWebSocketHandler handler = new DssWebSocketHandler();

        handler.channelRead(ctx, message);

        handler.channelReadComplete(ctx);

        Mockito.verify(ctx).flush();
    }

    @Test
    void exceptionCaught() {

        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssWebSocketHandler handler = new DssWebSocketHandler();

        handler.exceptionCaught(ctx, new NullPointerException());

        assertTrue(true);
    }
}
