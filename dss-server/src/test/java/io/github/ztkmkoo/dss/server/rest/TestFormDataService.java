package io.github.ztkmkoo.dss.server.rest;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorMultiPartFormDataService;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.util.CharsetUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 6. 17. 오후 10:06
 */
public class TestFormDataService extends DssRestActorMultiPartFormDataService {
    public TestFormDataService(String name, String path, DssRestMethodType methodType) {
        super(name, path, methodType);
    }

    @Override
    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Serializable>> request) {
        final Map<String, Serializable> testRequest = request.getBody();
        final String name = (String)testRequest.get("name");
        return new TestResponse(name);
    }
}
