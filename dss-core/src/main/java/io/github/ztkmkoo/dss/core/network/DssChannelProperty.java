package io.github.ztkmkoo.dss.core.network;

import java.io.Serializable;

public interface DssChannelProperty extends Serializable {

    String getHost();
    int getPort();
}
