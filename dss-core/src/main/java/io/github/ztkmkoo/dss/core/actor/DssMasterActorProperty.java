package io.github.ztkmkoo.dss.core.actor;

import io.github.ztkmkoo.dss.core.network.DssChannelProperty;

/**
 * Master property
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 02:51
 */
public interface DssMasterActorProperty {

    /**
     * Get netty channel property
     * @return {@link io.github.ztkmkoo.dss.core.network.DssChannelProperty}
     */
    DssChannelProperty getChannelProperty();
}
