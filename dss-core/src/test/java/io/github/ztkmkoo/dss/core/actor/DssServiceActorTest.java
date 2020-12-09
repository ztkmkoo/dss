package io.github.ztkmkoo.dss.core.actor;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.DssNettyChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.service.DssServiceRequest;
import io.github.ztkmkoo.dss.core.service.DssServiceResponse;
import io.github.ztkmkoo.dss.core.service.rest.AbstractDssRestService;
import io.github.ztkmkoo.dss.core.service.rest.DssRestRequestJsonConverter;
import io.github.ztkmkoo.dss.core.service.rest.DssRestResponseJsonConverter;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-10 04:40
 */
class DssServiceActorTest extends AbstractDssActorTest{

    @Test
    void handlingDssServiceRequestCommand() {
        final TestProbe<DssResolverCommand> resolverProbe = testKit.createTestProbe();
        final ActorRef<DssServiceCommand> serviceActor = testKit.spawn(DssServiceActor.create(TestService::new, DssRestServiceCommand.RestRequest.class));
        final DssChannelHandlerContext context = new DssNettyChannelHandlerContext(null);

        final DssRestResolverCommand.RestRequest originRequest = DssRestResolverCommand.RestRequest
                .builder()
                .ctx(context)
                .methodType(DssRestMethodType.GET)
                .contentType(DssRestContentType.APPLICATION_JSON)
                .path("/test")
                .content("{\"name\": \"Kebron\"}")
                .build();
        final DssRestServiceCommand.RestRequest request = DssRestServiceCommand.RestRequest.builder(originRequest, "testChannel",resolverProbe.getRef()).build();
        serviceActor.tell(request);

        final DssResolverCommand resolverCommand = resolverProbe.receiveMessage(Duration.ofSeconds(2));
        assertNotNull(resolverCommand);

        if (!(resolverCommand instanceof DssRestResolverCommand.RestResponse)) {
            fail();
        }

        final DssRestResolverCommand.RestResponse response = (DssRestResolverCommand.RestResponse) resolverCommand;
        assertEquals("testChannel", response.getChannelId());
        assertEquals(200, response.getStatus());
        assertEquals("{\"response\":\"Hello Kebron\"}", response.getBody());
    }

    private static class TestService extends AbstractDssRestService<TestRequest, TestResponse> {

        private static final String NAME = TestService.class.getSimpleName();
        private static final String PATH = "/test";
        private static final DssRestMethodType METHOD_TYPE = DssRestMethodType.GET;

        private TestService(ActorRef<DssServiceCommand> serviceActor) {
            super(
                    serviceActor,
                    NAME,
                    PATH,
                    METHOD_TYPE,
                    new DssRestRequestJsonConverter<>(new TypeReference<TestRequest>() {}),
                    new DssRestResponseJsonConverter<>()
            );
        }

        @Override
        public void start(TestRequest request) {
            final TestResponse response = new TestResponse();
            response.setSeq(request.getSeq());
            response.setResponse("Hello " + request.getName());

            end(response);
        }
    }

    @Getter @Setter
    public static class TestRequest implements DssServiceRequest {
        private static final long serialVersionUID = -9136394375046487263L;

        private long seq;

        private String name;

        public TestRequest() {
        }
    }

    @Getter @Setter
    public static class TestResponse implements DssServiceResponse {
        private static final long serialVersionUID = -6687497901160881898L;
        private long seq;
        private String response;

        public TestResponse() {
        }
    }
}