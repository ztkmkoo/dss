package io.github.ztkmkoo.dss.core.network.tcp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class DssTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(DssTcpChannelInitializer.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(new DssTcpHandler());
    }
}
