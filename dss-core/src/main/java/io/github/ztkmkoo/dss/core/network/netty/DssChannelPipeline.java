package io.github.ztkmkoo.dss.core.network.netty;

import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelPipeline;

public class DssChannelPipeline extends DefaultChannelPipeline {

    protected DssChannelPipeline(Channel channel) {
        super(channel);
    }
}
