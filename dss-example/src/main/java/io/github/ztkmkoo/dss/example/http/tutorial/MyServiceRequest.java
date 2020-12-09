package io.github.ztkmkoo.dss.example.http.tutorial;

import io.github.ztkmkoo.dss.core.service.DssServiceRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyServiceRequest implements DssServiceRequest {
    private static final long serialVersionUID = 1125940480805834052L;

    private long seq;

    private String name;

}