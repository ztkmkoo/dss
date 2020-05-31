package com.ztkmkoo.dss.server.rest;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.awaitility.Awaitility;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.*;
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
                await()
                        .atMost(10, TimeUnit.SECONDS)
                        .until(dssRestServer::isActivated);
                dssRestServer.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        dssRestServer.start();

        await()
                .atMost(15, TimeUnit.SECONDS)
                .until(dssRestServer::isShutdown);

        assertTrue(dssRestServer.isShutdown());
    }
}