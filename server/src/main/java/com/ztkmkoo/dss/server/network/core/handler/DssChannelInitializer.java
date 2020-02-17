package com.ztkmkoo.dss.server.network.core.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:11
 */
public abstract class DssChannelInitializer<T extends Channel> extends ChannelInitializer<T> {

    /**
     * initialize channel pipeline. (like add decoder or time request handler..)
     * @param p : netty ChannelPipeline
     */
    public abstract void initChannelPipeline(ChannelPipeline p);

    @Override
    protected void initChannel(T ch) throws Exception {

        final ChannelPipeline p = ch.pipeline();
        initChannelPipeline(p);
    }
}
