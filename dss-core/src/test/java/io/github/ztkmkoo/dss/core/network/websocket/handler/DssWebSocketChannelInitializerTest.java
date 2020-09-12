package io.github.ztkmkoo.dss.core.network.websocket.handler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;

class DssWebSocketChannelInitializerTest {

    @Mock
    private SocketChannel socketChannel;

    @Mock
    private ChannelPipeline channelPipeline;

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

        final DssWebSocketChannelInitializer dssWebSocketChannelInitializer = Mockito.mock(DssWebSocketChannelInitializer.class);

        dssWebSocketChannelInitializer.initChannel(socketChannel);

        final ChannelPipeline p = socketChannel.pipeline();

        assertEquals(HttpServerCodec.class, p.first().getClass());
        assertEquals(HttpObjectAggregator.class, p.get(HttpObjectAggregator.class).getClass());
        assertEquals(DssHttpRequestHandler.class, p.get(DssHttpRequestHandler.class).getClass());
        assertEquals(DssWebSocketHandler.class, p.last().getClass());
    }
}
