package io.github.ztkmkoo.dss.core.network.tcp.handler;

import org.slf4j.*;

import io.netty.channel.*;
import io.netty.channel.socket.*;
import io.netty.handler.codec.bytes.*;

public class DssTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(DssTcpChannelInitializer.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(new ByteArrayDecoder());
        p.addLast(new ByteArrayEncoder());
        p.addLast(new DssTcpHandler());
    }
}
