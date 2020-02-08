package com.ztkmkoo.dss.network.common.creator;

import com.ztkmkoo.dss.network.common.AbstractDssServer;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * @com.ztkmkoo.dss.network.enumeration.DssNetworkType for creating DssServer
 * It was for lambda expression, Do not make additional method
 */
public interface DssServerCreator {

    /**
     * for creating DssServer
     * @return AbstractDssServer(DssServer is okay too)
     */
    AbstractDssServer create();
}
