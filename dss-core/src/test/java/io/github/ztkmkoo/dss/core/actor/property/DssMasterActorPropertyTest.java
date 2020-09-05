package io.github.ztkmkoo.dss.core.actor.property;

import io.github.ztkmkoo.dss.core.service.DssService;
import io.github.ztkmkoo.dss.core.service.DssServiceGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:34
 */
class DssMasterActorPropertyTest {

    @Test
    void validateProperty() {
        final DssMasterActorProperty property = new TestProperty();
        assertEquals(1, property.getBossThreadCount());
        assertEquals(12, property.getWorkerThreadCount());

        assertNotNull(property.getServiceGeneratorList());
        assertTrue(property.getServiceGeneratorList().isEmpty());

        property.addDssServiceGenerator(new DssServiceGenerator() {
            @Override
            public DssService create() {
                return null;
            }
        });

        assertEquals(1, property.getServiceGeneratorList().size());
    }

    private static class TestProperty implements DssMasterActorProperty {

        private final List<DssServiceGenerator> list = new ArrayList<>();

        @Override
        public int getBossThreadCount() {
            return 1;
        }

        @Override
        public int getWorkerThreadCount() {
            return 12;
        }

        @Override
        public List<DssServiceGenerator> getServiceGeneratorList() {
            return list;
        }
    }
}