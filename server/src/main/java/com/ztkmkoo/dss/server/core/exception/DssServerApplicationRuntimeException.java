package com.ztkmkoo.dss.server.core.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:52
 */
public class DssServerApplicationRuntimeException extends RuntimeException {

    public DssServerApplicationRuntimeException(String message) {
        super(message);
    }

    public DssServerApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssServerApplicationRuntimeException(Throwable cause) {
        super(cause);
    }
}
