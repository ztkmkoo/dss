package io.github.ztkmkoo.dss.core.actor.property;

import java.util.Objects;

/**
 * Master property
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 02:51
 */
public interface DssMasterActorProperty {

    DssNetworkActorProperty getDssNetworkActorProperty();

    DssResolverActorProperty getDssResolverActorProperty();

    DssServiceActorProperty getDssServiceActorProperty();

    default void validateProperty() {
        Objects.requireNonNull(getDssNetworkActorProperty());
        Objects.requireNonNull(getDssResolverActorProperty());
        Objects.requireNonNull(getDssServiceActorProperty());
    }
}
