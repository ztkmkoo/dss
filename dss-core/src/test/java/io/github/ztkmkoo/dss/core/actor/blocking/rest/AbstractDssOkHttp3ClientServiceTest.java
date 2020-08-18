package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 23:56
 */
@Slf4j
class AbstractDssOkHttp3ClientServiceTest extends AbstractDssActorTest {

    @SuppressWarnings("unchecked")
    @Test
    void testGetGooglePage() {
        final TestHttpClientService service = new TestHttpClientService();
        final ActorRef<DssBlockingRestCommand> ref = testKit.spawn(DssHttpClientServiceActor.create(service));
        final TestProbe<DssBlockingRestCommand> probe = testKit.createTestProbe();

        ref.tell(DssBlockingRestCommand.HttpGetRequest.builder(33, probe.getRef(), "https://www.google.com").build());
        final DssBlockingRestCommand resCommand = probe.receiveMessage();
        assertTrue(resCommand instanceof DssBlockingRestCommand.HttpResponse);

        final DssBlockingRestCommand.HttpResponse<String> response =  (DssBlockingRestCommand.HttpResponse<String>) resCommand;
        assertNotNull(response);

        final String body = response.getBody();
        assertFalse(body.isEmpty());

        log.info("HttpResponse body: {}", body);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testHttpFailure() {
        final TestHttpClientService service = new TestHttpClientService();
        final ActorRef<DssBlockingRestCommand> ref = testKit.spawn(DssHttpClientServiceActor.create(service));
        final TestProbe<DssBlockingRestCommand> probe = testKit.createTestProbe();

        ref.tell(DssBlockingRestCommand.HttpGetRequest.builder(39, probe.getRef(), "https://localhost:8989").build());
        final DssBlockingRestCommand resCommand = probe.receiveMessage();
        assertTrue(resCommand instanceof DssBlockingRestCommand.HttpResponse);

        final DssBlockingRestCommand.HttpResponse<String> response =  (DssBlockingRestCommand.HttpResponse<String>) resCommand;
        assertNotNull(response);

        final String body = response.getBody();
        assertTrue(Objects.isNull(body));
        assertEquals(500, response.getCode());

        log.info("HttpResponse body: {}", body);
    }

    private static class TestHttpClientService extends AbstractDssOkHttp3ClientService<DssBlockingRestCommand.HttpGetRequest> {

        public TestHttpClientService() {
            super(1000, 1000);
        }

        @Override
        public String getName() {
            return "httpTestService";
        }

        @Override
        public Class<DssBlockingRestCommand.HttpGetRequest> getCommandClassType() {
            return DssBlockingRestCommand.HttpGetRequest.class;
        }

        @Override
        protected Request buildRequest(DssBlockingRestCommand.HttpGetRequest request) {
            return new Request.Builder()
                    .get()
                    .url(request.getUrl())
                    .build();
        }

        @Override
        protected Serializable convertBody(String body) {
            return body;
        }
    }
}