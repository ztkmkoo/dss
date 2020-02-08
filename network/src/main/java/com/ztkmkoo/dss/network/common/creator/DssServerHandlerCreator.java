package com.ztkmkoo.dss.network.common.creator;

import io.netty.channel.ChannelHandler;

public interface DssServerHandlerCreator {

    ChannelHandler createChannelHandler();
}
