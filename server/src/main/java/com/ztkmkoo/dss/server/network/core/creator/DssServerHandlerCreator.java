package com.ztkmkoo.dss.server.network.core.creator;

import io.netty.channel.ChannelHandler;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * For creating netty handler
 * It was for lambda expression, Do not make additional method
 */
public interface DssServerHandlerCreator {

    ChannelHandler createChannelHandler();
}
