package io.github.ztkmkoo.dss.core.actor.property;

import io.github.ztkmkoo.dss.core.service.DssServiceGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:54
 */
class DssServiceActorPropertyTest {

    @Test
    void addDssServiceGenerator() {
        final TestProperty property = new TestProperty();
        assertNotNull(property.getServiceGeneratorList());

        property.addDssServiceGenerator(() -> null);
        assertEquals(1, property.getServiceGeneratorList().size());
    }

    private static class TestProperty implements DssServiceActorProperty {

        private final List<DssServiceGenerator> list = new ArrayList<>();

        @Override
        public List<DssServiceGenerator> getServiceGeneratorList() {
            return list;
        }
    }
}