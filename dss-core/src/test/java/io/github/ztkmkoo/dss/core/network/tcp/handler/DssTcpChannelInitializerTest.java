package io.github.ztkmkoo.dss.core.network.tcp.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import io.netty.channel.*;
import io.netty.channel.socket.*;
import io.netty.handler.codec.bytes.*;

class DssTcpChannelInitializerTest {

    @Mock
    private SocketChannel socketChannel;

    @Mock
    private ChannelPipeline channelPipeline;

    @BeforeEach
    public void setUp() {
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
            .thenReturn(new ByteArrayDecoder());

        Mockito
            .when(channelPipeline.last())
            .thenReturn(new DssTcpHandler());

        final DssTcpChannelInitializer dssTcpChannelInitializer = Mockito.mock(DssTcpChannelInitializer.class);

        dssTcpChannelInitializer.initChannel(socketChannel);

        final ChannelPipeline p = socketChannel.pipeline();
        assertEquals(ByteArrayDecoder.class, p.first().getClass());
        assertEquals(DssTcpHandler.class, p.last().getClass());
    }
}