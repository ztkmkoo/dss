package io.github.ztkmkoo.dss.core.network.tcp.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.util.CharsetUtil;

class DssTcpHandlerTest {

    private  static final ByteBuf SEND_MESSAGE = Unpooled.copiedBuffer("Send Message", CharsetUtil.UTF_8);

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private Channel channel;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void channelActive() {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssTcpHandler handler = new DssTcpHandler();

        handler.channelActive(ctx);

        Mockito.verify(ctx).writeAndFlush(any());
    }

    @Test
    void channelRead0() throws Exception {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssTcpHandler handler = new DssTcpHandler();

        handler.channelRead0(ctx, SEND_MESSAGE);

        final ByteBuf firstMessage = getDssTcpHandlerFieldWithReflection(handler, "firstMessage", ByteBuf.class);
        assertEquals("Initial Message\n", firstMessage.toString(Charset.defaultCharset()));

        final StringBuilder buffer = getDssTcpHandlerFieldWithReflection(handler, "buffer", StringBuilder.class);
        assertEquals("Send Message", buffer.toString());
    }

    private static void mockChannelHandlerContextChannelId(ChannelHandlerContext ctx, String longId) {

        final Channel mockChannel = Mockito.mock(Channel.class);
        final ChannelId channelId = Mockito.mock(ChannelId.class);

        Mockito.when(ctx.channel()).thenReturn(mockChannel);
        Mockito.when(mockChannel.id()).thenReturn(channelId);
        Mockito.when(channelId.asLongText()).thenReturn(longId);
    }

    private static <T> T getDssTcpHandlerFieldWithReflection(DssTcpHandler handler, String fieldName, Class<T> tcLass) throws NoSuchFieldException, IllegalAccessException {

        final Field field = DssTcpHandler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return tcLass.cast(field.get(handler));
    }

    @Test
    void channelReadComplete() {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssTcpHandler handler = new DssTcpHandler();

        handler.channelReadComplete(ctx);

        Mockito.verify(ctx).flush();
    }

    @Test
    void exceptionCaught() {
        mockChannelHandlerContextChannelId(ctx, "abcedf");

        final DssTcpHandler handler = new DssTcpHandler();

        handler.exceptionCaught(ctx, new Throwable());

        Mockito.verify(ctx).close();
    }
}