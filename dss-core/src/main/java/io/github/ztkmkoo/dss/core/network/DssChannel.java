package io.github.ztkmkoo.dss.core.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-01 21:30
 */
public interface DssChannel<P extends DssChannelProperty, C extends ChannelInitializer> {

    /**
     *
     * @param serverBootstrap
     * @param property
     * @param channelInitializer
     * @return
     * @throws InterruptedException
     */
    Channel bind(ServerBootstrap serverBootstrap, P property, C channelInitializer) throws InterruptedException;
}
