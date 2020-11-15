package io.github.ztkmkoo.dss.core.actor.property;

import lombok.Getter;
import lombok.Setter;

/**
 * Master property
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 02:51
 */
public class DssMasterActorProperty implements DssActorProperty {
    private static final long serialVersionUID = -5126815092091781619L;

    // network
    @Getter @Setter
    private DssNetworkActorProperty networkActorProperty;

    // service
}
