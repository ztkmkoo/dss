package io.github.ztkmkoo.dss.example.http.tutorial;

import com.fasterxml.jackson.core.type.TypeReference;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

public class MyService extends DssRestActorJsonService<MyServiceRequest> {
    public MyService() {
        super(new TypeReference<MyServiceRequest>() {}, "myService", "/my/service", DssRestMethodType.GET);
    }

    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<MyServiceRequest> dssRestServiceRequest) {
        final String myName = dssRestServiceRequest.getBody().getName();
        final MyServiceResponse response = new MyServiceResponse();
        response.setResult("My service is " + myName + " service");
        return response;
    }
}
