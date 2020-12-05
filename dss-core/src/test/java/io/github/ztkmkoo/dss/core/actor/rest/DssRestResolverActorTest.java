package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.actor.DssServiceActorResolvable;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 16:21
 */
class DssRestResolverActorTest extends AbstractDssActorTest {

    private static DssRestResolverCommand.RestRequest defaultRequest(DssChannelHandlerContext context) {
        return DssRestResolverCommand.RestRequest
                .builder()
                .ctx(context)
                .methodType(DssRestMethodType.POST)
                .contentType(DssRestContentType.APPLICATION_JSON)
                .path("/test")
                .content("Hello")
                .build();
    }

    private static DssRestResolverCommand.RestResponse defaultResponse(String channelId, String body) {
        return DssRestResolverCommand.RestResponse
                .builder()
                .channelId(channelId)
                .status(HttpResponseStatus.OK.code())
                .body(body)
                .build();
    }

    private static DefaultFullHttpResponse validateResponseFromContext(TestDssChannelHandlerContext context, HttpResponseStatus expectedStatus) {
        await().atMost(2, TimeUnit.SECONDS).until(context::isReady);

        final HttpResponse response = context.getResponse();
        assertNotNull(response);
        assertEquals(expectedStatus, response.status());
        assertTrue(response instanceof DefaultFullHttpResponse);

        return (DefaultFullHttpResponse) response;
    }

    @Test
    void testHandlingRestRequestNotServed() {
        final ActorRef<DssResolverCommand> resolver = testKit.spawn(DssRestResolverActor.create());

        final TestDssChannelHandlerContext context = new TestDssChannelHandlerContext();
        final DssRestResolverCommand.RestRequest request = defaultRequest(context);

        resolver.tell(request);

        validateResponseFromContext(context, HttpResponseStatus.NOT_FOUND);
    }

    @Test
    void testHandlingRestRequest() {
        final TestProbe<DssServiceCommand> testServiceProbe = testKit.createTestProbe();
        final DssServiceActorResolvable<String> serviceActor = new DssRestServiceActorResolvable("/test", testServiceProbe.getRef());
        final ActorRef<DssResolverCommand> resolver = testKit.spawn(DssRestResolverActor.create(Collections.singletonList(serviceActor)));

        final TestDssChannelHandlerContext context = new TestDssChannelHandlerContext();
        final DssRestResolverCommand.RestRequest request = defaultRequest(context);

        resolver.tell(request);

        final DssServiceCommand serviceCommand = testServiceProbe.receiveMessage(Duration.ofSeconds(2));
        assertNotNull(serviceCommand);
        if (!(serviceCommand instanceof DssRestServiceCommand.RestRequest)) {
            fail();
        }

        final DssRestServiceCommand.RestRequest restRequest = (DssRestServiceCommand.RestRequest) serviceCommand;
        assertEquals(DssRestMethodType.POST, restRequest.getMethodType());
        assertEquals(DssRestContentType.APPLICATION_JSON, restRequest.getContentType());
        assertEquals("/test", restRequest.getPath());
        assertEquals("Hello", restRequest.getContent());
        assertEquals("test", restRequest.getChannelId());
        assertNotNull(restRequest.getSender());

        restRequest.getSender().tell(defaultResponse(restRequest.getChannelId(), "Response " + restRequest.getContent()));

        validateResponseFromContext(context, HttpResponseStatus.OK);
    }

    private static class TestDssChannelHandlerContext implements DssChannelHandlerContext {

        @Getter
        private boolean ready = false;

        @Getter
        private HttpResponse response;

        @Override
        public String getChannelId() {
            return "test";
        }

        @Override
        public void write(Object msg) {

        }

        @Override
        public void writeAndFlush(Object msg) {
            if (msg instanceof HttpResponse) {
                response = (HttpResponse) msg;
                ready = true;
            }
        }
    }
}