package io.github.ztkmkoo.dss.core.network.rest.enumeration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
}