package io.github.ztkmkoo.dss.core.actor.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:50
 */
class DssNetworkActorPropertyTest {

    @Test
    void normal() {
        final TestProperty1 property = new TestProperty1();
        assertEquals(1, property.getBossThreadCount());
        assertEquals(0, property.getWorkerThreadCount());
    }

    @Test
    void overrideMethod() {
        final TestProperty2 property = new TestProperty2();
        assertEquals(2, property.getBossThreadCount());
        assertEquals(16, property.getWorkerThreadCount());
    }

    private static class TestProperty1 implements DssNetworkActorProperty {
    }

    private static class TestProperty2 implements DssNetworkActorProperty {
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