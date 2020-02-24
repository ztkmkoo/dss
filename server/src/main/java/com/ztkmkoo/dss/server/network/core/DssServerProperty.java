package com.ztkmkoo.dss.server.network.core;

import com.ztkmkoo.dss.server.enumeration.DssNetworkType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * Property for running server
 */
@Deprecated
public interface DssServerProperty {

    /**
     * @return com.ztkmkoo.dss.server.enumeration.DssNetworkType
     */
    DssNetworkType getNetworkType();

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


}
