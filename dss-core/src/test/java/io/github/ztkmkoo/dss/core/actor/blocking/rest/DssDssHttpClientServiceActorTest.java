package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 02:23
 * @Override public void httpRequest(DssBlockingRestCommand.HttpGetRequest request, ActorRef<DssBlockingRestCommand> restActor) {
 * <p>
 * }
 */
class DssDssHttpClientServiceActorTest extends AbstractDssActorTest {

    @SuppressWarnings("unchecked")
    @Test
    void testHandlingHttpGetRequest() {
        final ActorRef<DssBlockingRestCommand> ref = testKit.spawn(DssHttpClientServiceActor.create(new TestService()));
        final TestProbe<DssBlockingRestCommand> probe = testKit.createTestProbe();

        final long seq = 33;
        ref.tell(DssBlockingRestCommand.HttpGetRequest
                .builder(seq, probe.getRef(), "https://www.github.com/test")
                .build());

        final DssBlockingRestCommand response = probe.receiveMessage();
        if (response instanceof DssBlockingRestCommand.HttpResponse) {
            final DssBlockingRestCommand.HttpResponse<String> testResponse = (DssBlockingRestCommand.HttpResponse<String>) response;
            assertEquals(33L, testResponse.getSeq());
            assertEquals(ref, testResponse.getSender());
            assertEquals(200, testResponse.getCode());
        } else {
            fail();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void testHandlingHttpGetRequestOnFailure() {
        final ActorRef<DssBlockingRestCommand> ref = testKit.spawn(DssHttpClientServiceActor.create(new TestFailureService()));
        final TestProbe<DssBlockingRestCommand> probe = testKit.createTestProbe();

        final long seq = 37;
        ref.tell(DssBlockingRestCommand.HttpGetRequest
                .builder(seq, probe.getRef(), "https://localhost:8989")
                .build());

        final DssBlockingRestCommand response = probe.receiveMessage();
        if (response instanceof DssBlockingRestCommand.HttpResponse) {
            final DssBlockingRestCommand.HttpResponse<String> testResponse = (DssBlockingRestCommand.HttpResponse<String>) response;
            assertEquals(37L, testResponse.getSeq());
            assertEquals(ref, testResponse.getSender());
            assertEquals(500, testResponse.getCode());
        } else {
            fail();
        }
    }

    private static class TestService implements DssHttpClientService<DssBlockingRestCommand.HttpGetRequest> {
        @Override
        public String getName() {
            return TestService.class.getSimpleName();
        }

        @Override
        public Class<DssBlockingRestCommand.HttpGetRequest> getCommandClassType() {
            return DssBlockingRestCommand.HttpGetRequest.class;
        }

        @Override
        public void httpRequest(DssBlockingRestCommand.HttpGetRequest request, ActorRef<DssBlockingRestCommand> restActor) {
            final DssBlockingRestCommand.HttpResponse<Serializable> res = DssBlockingRestCommand.HttpResponse
                    .builder(request.getSeq(), restActor, 200)
                    .message("OK")
                    .body("Hi")
                    .build();
            request.getSender().tell(res);
        }
    }

    private static class TestFailureService implements DssHttpClientService<DssBlockingRestCommand.HttpGetRequest> {
        @Override
        public String getName() {
            return TestFailureService.class.getSimpleName();
        }

        @Override
        public Class<DssBlockingRestCommand.HttpGetRequest> getCommandClassType() {
            return DssBlockingRestCommand.HttpGetRequest.class;
        }

        @Override
        public void httpRequest(DssBlockingRestCommand.HttpGetRequest request, ActorRef<DssBlockingRestCommand> restActor) {
            final DssBlockingRestCommand.HttpResponse<Serializable> res = DssBlockingRestCommand.HttpResponse
                    .builder(request.getSeq(), restActor, 500)
                    .message("Connection timeout")
                    .build();
            request.getSender().tell(res);
        }
    }
}