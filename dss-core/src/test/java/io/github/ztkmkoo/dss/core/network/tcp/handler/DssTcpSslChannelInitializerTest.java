package io.github.ztkmkoo.dss.core.network.tcp.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

class DssTcpSslChannelInitializerTest {

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
            .when(channelPipeline.last())
            .thenReturn(new DssTcpHandler());

        final DssTcpSslChannelInitializer dssTcpSslChannelInitializer = Mockito.mock(DssTcpSslChannelInitializer.class);

        dssTcpSslChannelInitializer.initChannel(socketChannel);

        final ChannelPipeline p = socketChannel.pipeline();
        assertEquals(DssTcpHandler.class, p.last().getClass());
    }
}