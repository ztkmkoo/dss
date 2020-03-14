package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import com.ztkmkoo.dss.server.rest.entity.DssRestServiceRequestWrapper;
import com.ztkmkoo.dss.server.rest.entity.DssRestServiceResponseWrapper;
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
            public DssRestServiceResponseWrapper handling(DssRestServiceRequestWrapper request) {
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