package io.github.ztkmkoo.dss.core.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import java.util.Objects;

public interface DssChannel<P extends DssChannelProperty, C extends Channel, I extends DssChannelInitializer<C>> {

    Channel bind(ServerBootstrap serverBootstrap, P dssChannelProperty, I dssChannelInitializer) throws InterruptedException;

    default void validate(ServerBootstrap serverBootstrap, P dssChannelProperty, I dssChannelInitializer) {
        Objects.requireNonNull(serverBootstrap);
        Objects.requireNonNull(dssChannelProperty);
        Objects.requireNonNull(dssChannelInitializer);
    }
}
