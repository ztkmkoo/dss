package com.ztkmkoo.dss.network.common;

import com.ztkmkoo.dss.network.enumeration.DssNetworkType;

public interface DssServerProperty {

    DssNetworkType getNetworkType();

    String getHost();

    int getPort();

    int getBossThread();

    int getWorkerThread();


}
