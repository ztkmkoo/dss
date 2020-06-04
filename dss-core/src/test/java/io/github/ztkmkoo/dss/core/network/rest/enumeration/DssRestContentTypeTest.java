package io.github.ztkmkoo.dss.core.network.rest.enumeration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 25. 오전 1:36
 */
public class DssRestContentTypeTest {

    @Test
    public void fromText() {

        final DssRestContentType type = DssRestContentType.fromText("application/json");
        assertEquals(DssRestContentType.APPLICATION_JSON, type);
    }
}