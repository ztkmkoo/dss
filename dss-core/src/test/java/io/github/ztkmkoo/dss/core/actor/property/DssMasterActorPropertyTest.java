package io.github.ztkmkoo.dss.core.actor.property;

import io.github.ztkmkoo.dss.core.service.DssServiceGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:34
 */
class DssMasterActorPropertyTest {

    @Test
    void validatePropertyThrowNullPointerException1() {
        final DssMasterActorProperty property = new TestProperty1();
        assertThrows(NullPointerException.class, property::validateProperty);
    }

    @Test
    void validatePropertyThrowNullPointerException2() {
        final DssMasterActorProperty property = new TestProperty2();
        assertThrows(NullPointerException.class, property::validateProperty);
    }

    @Test
    void validatePropertyThrowNullPointerException3() {
        final DssMasterActorProperty property = new TestProperty3();
        assertThrows(NullPointerException.class, property::validateProperty);
    }

    @Test
    void validateProperty() {
        final DssMasterActorProperty property = new TestProperty4();
        assertDoesNotThrow(property::validateProperty);
    }

    private static class TestProperty1 implements DssMasterActorProperty {

        @Override
        public DssNetworkActorProperty getDssNetworkActorProperty() {
            return null;
        }

        @Override
        public DssResolverActorProperty getDssResolverActorProperty() {
            return null;
        }

        @Override
        public DssServiceActorProperty getDssServiceActorProperty() {
            return null;
        }
    }

    private static class TestProperty2 implements DssMasterActorProperty {

        @Override
        public DssNetworkActorProperty getDssNetworkActorProperty() {
            return new DssNetworkActorProperty() {};
        }

        @Override
        public DssResolverActorProperty getDssResolverActorProperty() {
            return null;
        }

        @Override
        public DssServiceActorProperty getDssServiceActorProperty() {
            return null;
        }
    }

    private static class TestProperty3 implements DssMasterActorProperty {

        @Override
        public DssNetworkActorProperty getDssNetworkActorProperty() {
            return new DssNetworkActorProperty() {};
        }

        @Override
        public DssResolverActorProperty getDssResolverActorProperty() {
            return new DssResolverActorProperty() {};
        }

        @Override
        public DssServiceActorProperty getDssServiceActorProperty() {
            return null;
        }
    }

    private static class TestProperty4 implements DssMasterActorProperty {

        @Override
        public DssNetworkActorProperty getDssNetworkActorProperty() {
            return new DssNetworkActorProperty() {};
        }

        @Override
        public DssResolverActorProperty getDssResolverActorProperty() {
            return new DssResolverActorProperty() {};
        }

        @Override
        public DssServiceActorProperty getDssServiceActorProperty() {
            return new DssServiceActorProperty() {
                @Override
                public List<DssServiceGenerator> getServiceGeneratorList() {
                    return null;
                }
            };
        }
    }
}