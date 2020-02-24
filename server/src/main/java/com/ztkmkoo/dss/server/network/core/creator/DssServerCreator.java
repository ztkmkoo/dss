package com.ztkmkoo.dss.server.network.core.creator;

import com.ztkmkoo.dss.server.network.core.AbstractDssServer;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * @com.ztkmkoo.dss.network.enumeration.DssNetworkType for creating DssServer
 * It was for lambda expression, Do not make additional method
 */
@Deprecated
public interface DssServerCreator {

    /**
     * for creating DssServer
     * @return AbstractDssServer(DssServer is okay too)
     */
    AbstractDssServer create();
}
