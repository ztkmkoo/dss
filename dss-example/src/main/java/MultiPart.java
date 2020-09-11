import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.server.rest.DssRestServer;

import java.util.HashMap;

public class MultiPart {
    public static void main(String[] argv) throws InterruptedException {
        DssRestServer dss = new DssRestServer("127.0.0.1", 8181);

        dss.addDssService(new MyService("MyService", "/my", DssRestMethodType.GET));
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