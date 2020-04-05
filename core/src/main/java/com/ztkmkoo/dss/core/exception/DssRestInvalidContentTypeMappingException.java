package com.ztkmkoo.dss.core.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 1. 오전 4:54
 */
public class DssRestInvalidContentTypeMappingException extends RuntimeException {

    private static final long serialVersionUID = 6766253192416169562L;

    public DssRestInvalidContentTypeMappingException(String message) {
        super(message);
    }

    public DssRestInvalidContentTypeMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssRestInvalidContentTypeMappingException(Throwable cause) {
        super(cause);
    }
}
