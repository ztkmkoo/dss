package com.ztkmkoo.dss.core.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:05
 */
public class DssRestServiceMappingException extends RuntimeException {
    private static final long serialVersionUID = 5955150119271782542L;

    public DssRestServiceMappingException(String message) {
        super(message);
    }

    public DssRestServiceMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssRestServiceMappingException(Throwable cause) {
        super(cause);
    }
}
