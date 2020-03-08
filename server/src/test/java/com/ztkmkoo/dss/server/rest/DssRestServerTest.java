package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
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
        dssRestServer.addDssRestService(new AbstractDssRestService("/hi", DssRestMethodType.GET) {
            @Override
            public Object handling(Object request) {
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