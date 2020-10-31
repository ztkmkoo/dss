package io.github.ztkmkoo.dss.core.actor.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:34
 */
class DssMasterActorPropertyTest {

    @Test
    void validateProperty() {
        final DssMasterActorProperty property = new DssMasterActorProperty();

        assertNotNull(property);
    }
}