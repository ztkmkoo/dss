package io.github.ztkmkoo.dss.server.tcp;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.slf4j.*;

import io.github.ztkmkoo.dss.core.network.tcp.*;
import io.github.ztkmkoo.dss.core.network.tcp.handler.*;
import io.github.ztkmkoo.dss.core.network.tcp.property.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.handler.ssl.*;

public class DssTcpServer {

    private final Logger logger = LoggerFactory.getLogger(DssTcpServer.class);
    private final String host;
    private final int port;
    private final boolean ssl;
    private final SslContext sslContext;
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final AtomicBoolean shutdown = new AtomicBoolean(true);

    private Channel channel;

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
            channel = dssTcpChannel.bind(bootstrap, property, channelInitializer);
            active.set(true);
            shutdown.set(false);
            channel.closeFuture().sync();
        } finally {
            logger.info("Shut down worker and boss group gracefully");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            shutdown.set(true);
        }
    }

    public void stop() {
        if (Objects.nonNull(channel)) {
            logger.info("Channel try to close. [Active: {}][Open: {}]", channel.isActive(), channel.isOpen());
            channel.close();
            active.set(false);
            shutdown.set(true);
        }
    }

    public boolean isActivated() {
        return active.get();
    }

    public boolean isShutdown() {
        return shutdown.get();
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
