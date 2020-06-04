package io.github.ztkmkoo.dss.server.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.Serializable;
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

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    public void test() throws InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssRestService(new TestService("test", "/hi", DssRestMethodType.GET));

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Getter
    private static class TestResponse implements DssRestServiceResponse {
        private final String message;

        public TestResponse(String name) {
            this.message = "Hi " + name;
        }
    }

    @Getter @Setter
    private static class TestRequest implements Serializable {
        private String name;
    }

    private static class TestService extends DssRestActorJsonService<TestRequest> {

        public TestService(String name, String path, DssRestMethodType methodType) {
            super(new TypeReference<TestRequest>() {}, name, path, methodType);
        }

        @Override
        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestRequest> request) {
            final TestRequest testRequest = request.getBody();
            final String name = testRequest.getName();
            return new TestResponse(name);
        }
    }

    private static void startOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void stopDssRestServerAfterActivated(DssRestServer dssRestServer, int waitStartupSeconds, int waitShutdownSeconds) {
        startOnNewThread(() -> {
            try {
                await()
                        .atMost(waitStartupSeconds, TimeUnit.SECONDS)
                        .until(dssRestServer::isActivated);
                dssRestServer.stop();

                await()
                        .atMost(waitShutdownSeconds, TimeUnit.SECONDS)
                        .until(dssRestServer::isShutdown);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}