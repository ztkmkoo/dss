package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import lombok.Builder;
import lombok.Getter;
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
        final ActorRef<DssBlockingRestCommand> ref = testKit.spawn(DssHttpClientServiceActor.create(new TestService(), DssBlockingRestCommand.HttpGetRequest.class));
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

    private static class TestService implements DssHttpClientService<DssBlockingRestCommand.HttpGetRequest> {
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

    @Getter
    private static class TestResponse implements DssBlockingRestCommand.DssHttpResponseCommand<String> {
        private static final long serialVersionUID = -1320682451871656787L;

        private final int code;
        private final String message;
        private final String body;

        @Builder
        public TestResponse(int code, String message, String body) {
            this.code = code;
            this.message = message;
            this.body = body;
        }
    }
}