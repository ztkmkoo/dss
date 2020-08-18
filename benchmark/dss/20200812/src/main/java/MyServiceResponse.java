import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;

public class MyServiceResponse implements DssRestServiceResponse {
    private static final long serialVersionUID = -3631886302411908859L;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
