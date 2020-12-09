package io.github.ztkmkoo.dss.core.service;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.DssNettyChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.service.rest.AbstractDssRestService;
import io.github.ztkmkoo.dss.core.service.rest.DssRestRequestJsonConverter;
import io.github.ztkmkoo.dss.core.service.rest.DssRestResponseJsonConverter;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-10 05:26
 */
class AbstractDssServiceTest extends AbstractDssActorTest {

    private static DssRestServiceCommand.RestRequest defaultRequest(DssChannelHandlerContext context, TestProbe<DssResolverCommand> resolverProbe) {
        final DssRestResolverCommand.RestRequest originRequest = DssRestResolverCommand.RestRequest
                .builder()
                .ctx(context)
                .methodType(DssRestMethodType.GET)
                .contentType(DssRestContentType.APPLICATION_JSON)
                .path("/test")
                .content("{}")
                .build();

        return DssRestServiceCommand.RestRequest.builder(originRequest, "testChannel", resolverProbe.getRef()).build();
    }

    private final TestProbe<DssResolverCommand> resolverProbe = testKit.createTestProbe("resolverProbe");
    private final TestProbe<DssServiceCommand> serviceProbe = testKit.createTestProbe("serviceProbe");
    private final DssChannelHandlerContext context = new DssNettyChannelHandlerContext(null);

    @Test
    void startAndEnd() {
        final TestService testService = new TestService(serviceProbe.getRef());
        final DssRestServiceCommand.RestRequest request = defaultRequest(context, resolverProbe);

        testService.start(request);
        await().until(testService::isEnd);

        assertEquals(1L, testService.getSeq());

        final TestResponse testResponse = new TestResponse();
        testResponse.setSeq(testService.getSeq());
        testService.end(testResponse);

        final DssResolverCommand resolverCommand = resolverProbe.receiveMessage(Duration.ofSeconds(2));
        assertNotNull(resolverCommand);

        if (!(resolverCommand instanceof DssRestResolverCommand.RestResponse)) {
            fail();
        }

        final DssRestResolverCommand.RestResponse response = (DssRestResolverCommand.RestResponse) resolverCommand;
        assertEquals("testChannel", response.getChannelId());
        assertEquals(200, response.getStatus());
        assertEquals("{}", response.getBody());
    }

    @Test
    void getServiceActor() {
        final TestService testService = new TestService(serviceProbe.getRef());
        assertEquals("serviceProbe-2", testService.getServiceActor().path().name());
    }

    private static class TestService extends AbstractDssRestService<TestRequest, TestResponse> {

        private static final String NAME = TestService.class.getSimpleName();
        private static final String PATH = "/test";
        private static final DssRestMethodType METHOD_TYPE = DssRestMethodType.GET;

        @Getter
        private boolean end = false;
        @Getter
        private long seq;

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
            seq = request.getSeq();
            end = true;
        }
    }

    @Getter
    @Setter
    public static class TestRequest implements DssServiceRequest {
        private static final long serialVersionUID = -9136394375046487263L;

        private long seq;

        public TestRequest() {
        }
    }

    @Getter @Setter
    public static class TestResponse implements DssServiceResponse {
        private static final long serialVersionUID = -6687497901160881898L;
        private long seq;

        public TestResponse() {
        }
    }
}