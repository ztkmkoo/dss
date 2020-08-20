import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;


public class HelloService implements DssRestActorService {
    public String getName() {
        return "hello";
    }

    public String getPath() {
        return "/hello";
    }

    public DssRestMethodType getMethodType() {
        return DssRestMethodType.GET;
    }

    public DssRestContentInfo getConsume() {
        return DssRestContentInfo.APPLICATION_JSON_UTF8;
    }

    public DssRestContentInfo getProduce() {
        return DssRestContentInfo.APPLICATION_JSON_UTF8;
    }

    public DssRestServiceResponse handling(DssRestServiceActorCommandRequest commandRequest) {
        final String content = commandRequest.getContent();
        final MyServiceResponse response = new MyServiceResponse();
        response.setResult(content);
        return response;
    }
}