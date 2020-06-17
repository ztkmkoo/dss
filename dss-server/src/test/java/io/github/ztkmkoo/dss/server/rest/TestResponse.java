package io.github.ztkmkoo.dss.server.rest;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 6. 17. 오후 10:10
 */
@Getter
public class TestResponse implements DssRestServiceResponse {
    private final String message;

    public TestResponse(String name) {
        this.message = "Hi " + name;
    }
}
