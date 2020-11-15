package io.github.ztkmkoo.dss.core.actor.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:50
 */
class DssNetworkActorPropertyTest {

    @Test
    void testAll() {
        final DssNetworkActorProperty property = new DssNetworkActorProperty();
        property.setBossThreadCount(2);
        property.setWorkerThreadCount(16);

        assertEquals(2, property.getBossThreadCount());
        assertEquals(16, property.getWorkerThreadCount());
    }
}