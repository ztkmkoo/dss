package io.github.ztkmkoo.dss.core.actor.rest.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import org.junit.jupiter.api.Test;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:16
 */
class DssRestActorFormDataServiceTest {

    @Test
    void getBody() {

        final DssRestActorFormDataService service = new DssRestActorFormDataService("test", "/test", DssRestMethodType.GET) {
            @Override
            protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Object>> request) {
                return null;
            }
        };

        DssRestMasterActorCommand commandRequest = DssRestServiceActorCommandRequest.builder().build();
        final HashMap<String, Object> map1 = service.getBody((DssRestServiceActorCommandRequest) commandRequest);
        assertTrue(map1.isEmpty());

        commandRequest = DssRestServiceActorCommandRequest.builder()
                .content("id=kebron&password=1234567").build();
        final HashMap<String, Object> map2 = service.getBody((DssRestServiceActorCommandRequest) commandRequest);
        assertFalse(map2.isEmpty());
        assertEquals("kebron", map2.get("id"));
        assertEquals("1234567", map2.get("password"));
    }
}