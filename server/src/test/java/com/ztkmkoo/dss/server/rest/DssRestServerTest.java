package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import com.ztkmkoo.dss.server.rest.service.AbstractDssRestService;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 8. 오후 4:59
 */
public class DssRestServerTest {

    @Test
    public void start() throws Exception {

        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer.addDssRestService(new AbstractDssRestService("test", "/hi", DssRestMethodType.GET) {

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
        });
        new Thread(() -> {
            try {
                dssRestServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        dssRestServer.stop();
        assertTrue(true);
    }
}