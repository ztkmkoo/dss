package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.net.ssl.SSLEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;

class DssWebSocketSslChannelInitializerTest {

    @Mock
    private SocketChannel socketChannel;

    @Mock
    private ChannelPipeline channelPipeline;

    @Mock
    private SSLEngine sslEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void initChannel() throws Exception {

        Mockito
                .when(socketChannel.pipeline())
                .thenReturn(channelPipeline);

        Mockito
                .when(channelPipeline.addLast(any()))
                .thenReturn(channelPipeline);

        Mockito
                .when(channelPipeline.first())
                .thenReturn(new SslHandler(sslEngine));

        Mockito
                .when(channelPipeline.get(HttpServerCodec.class))
                .thenReturn(new HttpServerCodec());

        Mockito
                .when(channelPipeline.get(HttpObjectAggregator.class))
                .thenReturn(new HttpObjectAggregator(65536));

        Mockito
                .when(channelPipeline.get(DssHttpRequestHandler.class))
                .thenReturn(new DssHttpRequestHandler("/websocket"));

        Mockito
                .when(channelPipeline.last())
                .thenReturn(new DssWebSocketHandler());

        final DssWebSocketSslChannelInitializer dssWebSocketSslChannelInitializerTest = Mockito.mock(DssWebSocketSslChannelInitializer.class);

        dssWebSocketSslChannelInitializerTest.initChannel(socketChannel);

        final ChannelPipeline p = socketChannel.pipeline();

        assertEquals(SslHandler.class, p.first().getClass());
        assertEquals(HttpServerCodec.class, p.get(HttpServerCodec.class).getClass());
        assertEquals(HttpObjectAggregator.class, p.get(HttpObjectAggregator.class).getClass());
        assertEquals(DssHttpRequestHandler.class, p.get(DssHttpRequestHandler.class).getClass());
        assertEquals(DssWebSocketHandler.class, p.last().getClass());
    }
}
