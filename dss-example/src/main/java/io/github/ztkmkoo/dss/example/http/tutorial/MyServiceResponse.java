package io.github.ztkmkoo.dss.example.http.tutorial;

import io.github.ztkmkoo.dss.core.service.DssServiceResponse;
import lombok.Getter;
import lombok.Setter;

public class MyServiceResponse implements DssServiceResponse {

    @Getter
    private long seq;

    @Getter @Setter
    private String result;
}