package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 8. 오후 5:40
 */
public class AbstractDssRestServiceTest {

    @Test
    public void abstractDssRestService() {
        final AbstractDssRestService dssRestService = new AbstractDssRestService("/hi", DssRestMethodType.POST) {
            @Override
            public Object handling(Object request) {
                return null;
            }
        };

        assertNotNull(dssRestService);
        assertEquals("/hi", dssRestService.getUri());
        assertEquals(DssRestMethodType.POST, dssRestService.getMethodType());
    }
}