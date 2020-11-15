package io.github.ztkmkoo.dss.core.network.rest.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 25. 오전 1:36
 */
class DssRestContentTypeTest {

    @Test
    void fromText() {
        final DssRestContentType type = DssRestContentType.fromText("application/json");
        assertEquals(DssRestContentType.APPLICATION_JSON, type);
    }

    @Test
    void testNotExist() {
        final DssRestContentType type = DssRestContentType.fromText("application/jsonT");
        assertNull(type);
    }

    @Test
    void testNotExist2() {
        final DssRestContentType type = DssRestContentType.fromText(null);
        assertNull(type);
    }
}