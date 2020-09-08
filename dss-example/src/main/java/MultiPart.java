import com.fasterxml.jackson.core.type.TypeReference;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.server.rest.DssRestServer;

import java.io.Serializable;
import java.util.HashMap;

public class MultiPart {
    public static void main(String[] argv) throws InterruptedException {
        DssRestServer dss = new DssRestServer("127.0.0.1", 8181);

        dss.addDssService(new MyService("MyService", "/my", DssRestMethodType.GET));
        dss.addDssService(new YourService());
        dss.start();
    }
}

class MyService extends DssRestActorFormDataService {
    public MyService(String name, String path, DssRestMethodType methodType) {
        super(name, path, methodType);
    }

    @Override
    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Object>> request) {
        MyServiceResponse myResponse = new MyServiceResponse();
        myResponse.setValues(request.getBody());

        return myResponse;
    }
}

class MyServiceResponse implements DssRestServiceResponse {
    private HashMap<String, Object> values;

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public HashMap<String, Object> getValues() {
        return values;
    }
}

class YourServiceRequest implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class YourServiceResponse implements DssRestServiceResponse {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

class YourService extends DssRestActorJsonService<YourServiceRequest> {
    public YourService() {
        super(new TypeReference<YourServiceRequest>() {}, "YourService", "/your", DssRestMethodType.GET);
    }

    @Override
    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<YourServiceRequest> request) {
        final String myName = request.getBody().getName();
        final YourServiceResponse response = new YourServiceResponse();
        response.setResult("Your service is " + myName + " service");
        return response;
    }
}