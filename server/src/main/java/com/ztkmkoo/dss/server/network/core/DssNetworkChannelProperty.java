package com.ztkmkoo.dss.server.network.core;

import com.ztkmkoo.dss.server.network.core.creator.DssChannelInitializerCreator;
import com.ztkmkoo.dss.server.network.core.enumeration.NettyLogLevelWrapperType;
import com.ztkmkoo.dss.server.network.core.handler.DssChannelInitializer;
import com.ztkmkoo.dss.server.network.core.service.DssServiceCreator;
import io.netty.channel.Channel;

import java.util.List;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:32
 */
public interface DssNetworkChannelProperty {

    /**
     * @return server binding host, like 127.0.0.1
     */
    String getHost();

    /**
     * @return server binding port, like 443
     */
    int getPort();

    /**
     * @return netty parent EventLoopGroup thread.  Default is availableProcessors * 2
     */
    int getBossThread();

    /**
     * @return netty handler EventLoopGroup thread. Default is availableProcessors * 2
     */
    int getWorkerThread();

    /**
     * @return : channel service creator list.
     */
    List<DssServiceCreator> getChannelServiceCreatorList();

    /**
     * @return : netty handler or module log level
     */
    NettyLogLevelWrapperType getLogLevel();

    /**
     * @return : DssChannelInitializer
     */
    @Deprecated
    <T extends Channel> DssChannelInitializer<T> getDssChannelInitializer();

    /**
     * @return : DssChannelInitializerCreator
     */
    DssChannelInitializerCreator getDssChannelInitializerCreator();
}
