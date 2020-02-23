package com.ztkmkoo.dss.server.network.rest.entity;

import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 24. 오전 12:19
 */
public class DssRestSuccessResponse implements DssRestResponse {

    @Getter
    private final int status;
    @Getter
    private final String message;

    public DssRestSuccessResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DssRestSuccessResponse(int status) {
        this(status, null);
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    @Override
    public String toString() {
        return "DssRestSuccessResponse {\n" +
                "status=" + status + ",\n" +
                "message=" + message + ",\n" +
                "successful=true\n" +
                '}';
    }
}
