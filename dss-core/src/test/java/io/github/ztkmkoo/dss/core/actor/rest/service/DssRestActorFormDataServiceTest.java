package io.github.ztkmkoo.dss.core.actor.rest.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

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

        final HashMap<String, Object> map1 = service.getBody("");
        assertTrue(map1.isEmpty());

        final HashMap<String, Object> map2 = service.getBody("id=kebron&password=1234567");
        assertFalse(map2.isEmpty());
        assertEquals("kebron", map2.get("id"));
        assertEquals("1234567", map2.get("password"));
    }
}