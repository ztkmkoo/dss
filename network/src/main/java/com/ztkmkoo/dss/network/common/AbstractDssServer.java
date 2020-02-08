package com.ztkmkoo.dss.network.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Objects;
import java.util.logging.Logger;

public abstract class AbstractDssServer implements DssServer {

    protected final Logger logger;

    protected AbstractDssServer() {

        this.logger = Logger.getLogger(this.getClass().getSimpleName());
    }

    protected abstract ServerBootstrap configServerBootstrap(DssServerProperty p, ServerBootstrap b) throws InterruptedException;

    @Override
    public void bind(DssServerProperty property) throws InterruptedException {

        Objects.requireNonNull(property, "DssServerProperty is null");

        final EventLoopGroup boosGroup = new NioEventLoopGroup(property.getBossThread());
        final EventLoopGroup workerGroup = new NioEventLoopGroup(property.getWorkerThread());

        try {
            final ServerBootstrap b = new ServerBootstrap().group(boosGroup, workerGroup);
            configServerBootstrap(property, b)
                    .bind(property.getHost(), property.getPort())
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
