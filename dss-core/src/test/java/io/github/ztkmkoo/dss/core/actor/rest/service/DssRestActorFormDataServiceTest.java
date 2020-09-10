package io.github.ztkmkoo.dss.core.actor.rest.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.message.rest.*;
import org.junit.jupiter.api.Test;
import akka.actor.testkit.typed.javadsl.TestProbe;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:16
 */
public class DssRestActorFormDataServiceTest {
    final ActorTestKit testKit = ActorTestKit.create();
    final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe(DssRestChannelHandlerCommand.class);


    @Test
    void getBody() {

        final DssRestActorFormDataService service = new DssRestActorFormDataService("test", "/test", DssRestMethodType.GET) {
            @Override
            protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Object>> request) {
                return null;
            }
        };

        DssRestServiceActorCommandRequest sampleCommandRequest = new DssRestServiceActorCommandRequest(DssRestMasterActorCommandRequest
                .builder()
                .channelId("testChannelId")
                .path("/test").methodType(DssRestMethodType.GET).sender(probe.getRef())
                .content("id=kebron&password=1234567").build());

        final HashMap<String, Object> map2 = service.getBody(sampleCommandRequest);
        assertFalse(map2.isEmpty());
        assertEquals("kebron", map2.get("id"));
        assertEquals("1234567", map2.get("password"));
    }
}