package com.ztkmkoo.dss.server.network.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:32
 */
public interface DssNetworkChannel {

    Channel bind(ServerBootstrap b, DssNetworkChannelProperty property) throws InterruptedException;
}
