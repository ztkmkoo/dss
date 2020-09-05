package io.github.ztkmkoo.dss.core.actor.property;

/**
 * Master property
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 02:51
 */
public interface DssMasterActorProperty {

    DssNetworkActorProperty getNetworkActorProperty();

    DssResolverActorProperty getResolverActorProperty();

    DssServiceActorProperty getServiceActorProperty();
}
