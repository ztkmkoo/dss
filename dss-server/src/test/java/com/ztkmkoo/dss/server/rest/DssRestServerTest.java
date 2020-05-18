package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.Test;

import java.util.HashMap;

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
        dssRestServer
                .addDssRestService(new DssRestActorJsonService("test", "/hi", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                        return null;
                    }
                })
                .addDssRestService(new DssRestActorFormDataService("test2", "/hello", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Object>> request) {
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