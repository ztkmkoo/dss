package io.github.ztkmkoo.dss.server.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ztkmkoo.dss.core.network.tcp.DssTcpChannel;
import io.github.ztkmkoo.dss.core.network.tcp.handler.DssTcpChannelInitializer;
import io.github.ztkmkoo.dss.core.network.tcp.handler.DssTcpSslChannelInitializer;
import io.github.ztkmkoo.dss.core.network.tcp.property.DssTcpChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;

public class DssTcpServer {

    private final Logger logger = LoggerFactory.getLogger(DssTcpServer.class);
    private final String host;
    private final int port;
    private final boolean ssl;
    private final SslContext sslContext;

    public DssTcpServer(String host, int port) {
        this(host, port, false, null);
    }

    public DssTcpServer(String host, int port, boolean ssl, SslContext sslContext) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.sslContext = sslContext;
    }

    public void start() throws InterruptedException {
        final DssTcpChannelInitializer channelInitializer = dssTcpChannelInitializer();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final DssTcpChannelProperty property = newDssTcpChannelProperty();

        try {
            final DssTcpChannel dssTcpChannel = new DssTcpChannel();
            Channel channel = dssTcpChannel.bind(bootstrap, property, channelInitializer);
            channel.closeFuture().sync();
        } finally {
            logger.info("Shut down worker and boss group gracefully");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private DssTcpChannelProperty newDssTcpChannelProperty() {
        return DssTcpChannelProperty
            .builder()
            .host(host)
            .port(port)
            .build();
    }

    private DssTcpChannelInitializer dssTcpChannelInitializer() throws InterruptedException {
        return ssl ? new DssTcpSslChannelInitializer(sslContext) : new DssTcpChannelInitializer();
    }
}
