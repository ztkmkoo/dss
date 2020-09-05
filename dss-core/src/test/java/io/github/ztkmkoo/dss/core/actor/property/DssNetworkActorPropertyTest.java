package io.github.ztkmkoo.dss.core.actor.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:50
 */
class DssNetworkActorPropertyTest {

    @Test
    void testAll() {
        final TestProperty property = new TestProperty();
        assertEquals(2, property.getBossThreadCount());
        assertEquals(16, property.getWorkerThreadCount());
    }

    private static class TestProperty implements DssNetworkActorProperty {
        @Override
        public int getBossThreadCount() {
            return 2;
        }

        @Override
        public int getWorkerThreadCount() {
            return 16;
        }
    }
}