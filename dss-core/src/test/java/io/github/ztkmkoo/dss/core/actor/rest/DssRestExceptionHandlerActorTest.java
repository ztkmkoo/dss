package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.actor.exception.DssRestRequestMappingException;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.exception.annotation.ExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.annotation.ServiceExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.handler.DssExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.handler.DssRestExceptionHandlerResolver;
import io.github.ztkmkoo.dss.core.message.rest.*;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DssRestExceptionHandlerActorTest extends AbstractDssActorTest {

    private static final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();
    private static final DssRestServiceActorCommandRequest commandRequest = new DssRestServiceActorCommandRequest(DssRestServiceActorCommandRequest
            .builder()
            .channelId("test")
            .sender(probe.ref())
            .methodType(DssRestMethodType.GET)
            .path("/test")
            .build());
    private static ActorRef<DssRestExceptionHandlerCommand> restExceptionHandlerActorRef;

    @BeforeAll
    static void setRestExceptionHandlerActorRef() {
        DssRestExceptionHandlerResolver.getInstance().setExceptionHandlerMap(new TestExceptionHandler());
        restExceptionHandlerActorRef = testKit.spawn(DssRestExceptionHandlerActor.create(DssRestExceptionHandlerResolver.getInstance().getExceptionHandlerMap()), "exception-handler");
    }

    @Test
    void handleException() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(testService())
                .request(commandRequest)
                .exception(new Exception())
                .build());

        DssRestChannelHandlerCommandResponse response = probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);

        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), response.getStatus().intValue());
        assertEquals("test", response.getChannelId());
    }

    @Test
    void handleDssRestServiceMappingException() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(testService())
                .request(commandRequest)
                .exception(new DssRestRequestMappingException("test"))
                .build());

        DssRestChannelHandlerCommandResponse response = probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);

        assertEquals(HttpResponseStatus.BAD_REQUEST.code(), response.getStatus().intValue());
        assertEquals("test", response.getChannelId());
    }

    @Test
    void globalHandlingExceptionTest() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(new DssRestActorJsonService("hi", "/test", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                        return null;
                    }
                })
                .request(commandRequest)
                .exception(new NullPointerException())
                .build());

        DssRestChannelHandlerCommandResponse response = probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);

        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), response.getStatus().intValue());
        assertEquals("test", response.getChannelId());
        assertEquals("globalExceptionHandleMethod", response.getResponse().toString());
    }

    @Test
    void serviceHandlingExceptionTest() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(testService())
                .request(commandRequest)
                .exception(new NullPointerException())
                .build());

        DssRestChannelHandlerCommandResponse response = probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);

        assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), response.getStatus().intValue());
        assertEquals("test", response.getChannelId());
        assertEquals("serviceExceptionHandleMethod", response.getResponse().toString());
    }

    @Test
    void OccurExceptionWhileHandlingExceptionTest() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(testService())
                .request(commandRequest)
                .exception(new DssRestRequestMappingException("mapping exception"))
                .build());

        DssRestChannelHandlerCommandResponse response = probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);

        assertEquals(HttpResponseStatus.BAD_REQUEST.code(), response.getStatus().intValue());
        assertEquals("test", response.getChannelId());
        assertNull(response.getResponse());
    }

    private DssRestActorJsonService<TestRequest> testService() {
        return new TestService("hi", "/test", DssRestMethodType.GET);
    }

    @Getter @Setter
    private static class TestRequest implements Serializable {
        private static final long serialVersionUID = 6373259023479826730L;

        private String name;
    }

    static private class TestService extends DssRestActorJsonService<TestRequest> {

        public TestService(String name, String path, DssRestMethodType methodType) {
            super(name, path, methodType);
        }

        @Override
        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestRequest> request) {
            return null;
        }
    }

    static private class TestExceptionHandler implements DssExceptionHandler {

        @ExceptionHandler(exception = NullPointerException.class)
        public DssRestServiceResponse globalExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
            return new globalResponse("globalExceptionHandleMethod");
        }

        @ExceptionHandler(exception = DssRestRequestMappingException.class)
        public DssRestServiceResponse testForExceptionWhileRunningExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
            throw new RuntimeException("testForExceptionWhileRunningExceptionHandleMethod");
        }

        @ServiceExceptionHandler(service = TestService.class, exception = NullPointerException.class)
        public DssRestServiceResponse serviceExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
            return new globalResponse("serviceExceptionHandleMethod");
        }

        @Getter
        @Setter
        static class globalResponse implements DssRestServiceResponse {
            private static final long serialVersionUID = -8811453587826373582L;
            private String result;

            public globalResponse(String result) {
                this.result = result;
            }

            @Override
            public String toString() {
                return result;
            }
        }
    }
}
