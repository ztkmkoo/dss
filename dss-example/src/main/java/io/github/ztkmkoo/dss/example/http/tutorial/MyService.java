package io.github.ztkmkoo.dss.example.http.tutorial;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.service.rest.AbstractDssRestService;

public class MyService extends AbstractDssRestService<MyServiceRequest, MyServiceResponse> {

    public MyService() {
        super("myService", "/my/service", DssRestMethodType.GET, null, null);
    }

    @Override
    public void start(MyServiceRequest request) {
        final String myName = request.getName();
        final MyServiceResponse response = new MyServiceResponse();
        response.setResult("My service is " + myName + " service");

        end(response);
    }
}
