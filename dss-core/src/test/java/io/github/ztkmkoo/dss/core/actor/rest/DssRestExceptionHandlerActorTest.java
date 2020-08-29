package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.exception.DssRestServiceMappingException;
import io.github.ztkmkoo.dss.core.exception.handler.DssRestExceptionHandlerResolver;
import io.github.ztkmkoo.dss.core.message.rest.*;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DssRestExceptionHandlerActorTest extends AbstractDssActorTest {

    private static final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();
    private static final ActorRef<DssRestExceptionHandlerCommand> restExceptionHandlerActorRef = testKit.spawn(DssRestExceptionHandlerActor.create(DssRestExceptionHandlerResolver.getInstance().getExceptionHandlerMap()), "rest-master");
    private static final DssRestServiceActorCommandRequest commandRequest = new DssRestServiceActorCommandRequest(DssRestServiceActorCommandRequest
            .builder()
            .channelId("test")
            .sender(probe.ref())
            .methodType(DssRestMethodType.GET)
            .path("/test")
            .build());

    @Test
    void handleException() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(testService())
                .request(commandRequest)
                .exception(new Exception())
                .build());

        probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);
        assertTrue(true);
    }

    @Test
    void handleDssRestServiceMappingException() {
        restExceptionHandlerActorRef.tell(DssRestExceptionHandlerCommandRequest
                .builder()
                .service(testService())
                .request(commandRequest)
                .exception(new DssRestServiceMappingException("test"))
                .build());

        probe.expectMessageClass(DssRestChannelHandlerCommandResponse.class);
        assertTrue(true);
    }

    private DssRestActorService testService(){
        return new DssRestActorJsonService("hi", "/test", DssRestMethodType.GET) {

            @Override
            protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                return null;
            }
        };
    }
}
