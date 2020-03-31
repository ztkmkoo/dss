package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import com.ztkmkoo.dss.server.rest.service.AbstractDssRestService;
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
        final AbstractDssRestService dssRestService = new AbstractDssRestService("test", "/hi", DssRestMethodType.POST) {

            @Override
            public DssRestContentInfo getConsume() {
                return null;
            }

            @Override
            public DssRestContentInfo getProduce() {
                return null;
            }

            @Override
            public DssRestServiceResponse handling(DssRestServiceRequest request) {
                return null;
            }

            @Override
            public DssRestServiceRequest convertRequest(DssRestServiceActorCommandRequest commandRequest) {
                return null;
            }
        };

        assertNotNull(dssRestService);
        assertEquals("/hi", dssRestService.getPath());
        assertEquals(DssRestMethodType.POST, dssRestService.getMethodType());
    }
}