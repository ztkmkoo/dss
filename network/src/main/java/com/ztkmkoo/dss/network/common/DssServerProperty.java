package com.ztkmkoo.dss.network.common;

import com.ztkmkoo.dss.network.enumeration.DssNetworkType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public interface DssServerProperty {

    DssNetworkType getNetworkType();

    String getHost();

    int getPort();

    int getBossThread();

    int getWorkerThread();


}
