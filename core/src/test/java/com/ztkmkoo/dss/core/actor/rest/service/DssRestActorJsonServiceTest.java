package com.ztkmkoo.dss.core.actor.rest.service;

import akka.actor.testkit.typed.javadsl.TestProbe;
import com.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequestJsonImpl;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.util.CharsetUtil;
import lombok.Builder;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 30. 오전 2:41
 */
public class DssRestActorJsonServiceTest extends AbstractDssActorTest {

    private static final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();
    private static final DssRestServiceActorCommandRequest sampleCommandRequest = new DssRestServiceActorCommandRequest(DssRestMasterActorCommandRequest
            .builder()
            .channelId("testChannelId")
            .path("/test")
            .methodType(DssRestMethodType.GET)
            .sender(probe.getRef())
            .content("{\"age\":15, \"point\": {\"type1\": 100, \"type2\": 500}}")
            .build());

    private final TestService testService = TestService
            .builder()
            .name("test")
            .path("/test")
            .methodType(DssRestMethodType.GET)
            .build();

    @Test
    public void handling() {
        final DssRestServiceRequest<HashMap<String, Serializable>> requestJson = testService.convertRequest(sampleCommandRequest);
        final DssRestServiceResponse response = testService.handling(requestJson);
        assertNull(response);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void convertRequest() {
        final DssRestServiceRequestJsonImpl<HashMap<String, Serializable>> requestJson = testService.convertRequest(sampleCommandRequest);

        final HashMap<String, Serializable> requestBody = requestJson.getBody();
        assertNotNull(requestBody);
        assertFalse(requestBody.isEmpty());

        final int age = (int)requestBody.get("age");
        assertEquals(15, age);

        final LinkedHashMap<String, Integer> pointMap = (LinkedHashMap<String, Integer>) requestBody.get("point");
        assertNotNull(pointMap);
        assertFalse(pointMap.isEmpty());

        final int point1 = pointMap.get("type1");
        final int point2 = pointMap.get("type2");
        assertEquals(100, point1);
        assertEquals(500, point2);
    }

    @Test
    public void getName() {
        assertEquals("test", testService.getName());
    }

    @Test
    public void getPath() {
        assertEquals("/test", testService.getPath());
    }

    @Test
    public void getMethodType() {
        assertEquals(DssRestMethodType.GET, testService.getMethodType());
    }

    @Test
    public void getConsume() {
        assertNotNull(testService.getConsume());
        assertEquals(DssRestContentType.APPLICATION_JSON, testService.getConsume().getContentType());
        assertEquals(CharsetUtil.UTF_8, testService.getConsume().getCharset());
    }

    @Test
    public void getProduce() {
        assertNotNull(testService.getProduce());
        assertEquals(DssRestContentType.APPLICATION_JSON, testService.getProduce().getContentType());
        assertEquals(CharsetUtil.UTF_8, testService.getProduce().getCharset());
    }


    private static class TestService extends DssRestActorJsonService<HashMap<String, Serializable>> {

        @Builder
        protected TestService(String name, String path, DssRestMethodType methodType) {
            super(name, path, methodType);
        }

        @Override
        protected DssRestServiceResponse handling(DssRestServiceRequestJsonImpl<HashMap<String, Serializable>> request) {
            return null;
        }
    }
}