package io.github.ztkmkoo.dss.core.actor.property;

import java.util.Objects;

/**
 * Master property
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 02:51
 */
public interface DssMasterActorProperty {

    DssNetworkActorProperty getNetworkActorProperty();

    DssResolverActorProperty getResolverActorProperty();

    DssServiceActorProperty getServiceActorProperty();

    default void validateProperty() {
        Objects.requireNonNull(getNetworkActorProperty());
        Objects.requireNonNull(getResolverActorProperty());
        Objects.requireNonNull(getServiceActorProperty());
    }
}
