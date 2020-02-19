package com.ztkmkoo.dss.server.network.rest.entity;

import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 20. 오전 12:08
 */
public class DssRestErrorResponse implements DssRestResponse {

    @Getter
    private final int status;
    @Getter
    private final String message;

    public DssRestErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DssRestErrorResponse(int status) {
        this(status, null);
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
