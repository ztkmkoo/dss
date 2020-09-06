package io.github.ztkmkoo.dss.core.actor.property;

import io.github.ztkmkoo.dss.core.service.DssServiceGenerator;

import java.util.List;
import java.util.Objects;

/**
 * Master property
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 02:51
 */
public interface DssMasterActorProperty {

    // network

    /**
     * netty boss event loop thread count
     */
    int getBossThreadCount();

    /**
     * netty worker event loop thread count
     */
    int getWorkerThreadCount();

    // service

    List<DssServiceGenerator> getServiceGeneratorList();

    // default

    default void addDssServiceGenerator(DssServiceGenerator generator) {
        final List<DssServiceGenerator> list = getServiceGeneratorList();
        if (Objects.nonNull(list) && Objects.nonNull(generator)) {
            list.add(generator);
        }
    }
}
