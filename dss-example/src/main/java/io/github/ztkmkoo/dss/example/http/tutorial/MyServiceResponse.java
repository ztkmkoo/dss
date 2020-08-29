package io.github.ztkmkoo.dss.example.http.tutorial;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;

public class MyServiceResponse implements DssRestServiceResponse {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}