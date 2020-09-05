package io.github.ztkmkoo.dss.core.actor.property;

import io.github.ztkmkoo.dss.core.service.DssServiceGenerator;

import java.util.List;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 01:29
 */
public interface DssServiceActorProperty {

    List<DssServiceGenerator> getServiceGeneratorList();

    default void addDssServiceGenerator(DssServiceGenerator generator) {
        final List<DssServiceGenerator> list = getServiceGeneratorList();
        if (Objects.nonNull(list) && Objects.nonNull(generator)) {
            list.add(generator);
        }
    }
}
