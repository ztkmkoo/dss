package io.github.ztkmkoo.dss.core.actor.rest.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;

import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import org.junit.jupiter.api.Test;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:26
 */
public class AbstractDssRestActorServiceTest {

    @Test
    public void testConstructor() {
        final AbstractDssRestActorService service = new AbstractDssRestActorService("test", "test", DssRestMethodType.GET) {
            @Override
            protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                return null;
            }

            @Override
            protected Serializable getBody(DssRestServiceActorCommandRequest commandRequest) {
                return null;
            }
        };

        assertEquals(DssRestContentType.APPLICATION_JSON, service.getConsume().getContentType());
        assertEquals(DssRestContentType.APPLICATION_JSON, service.getProduce().getContentType());
    }
}