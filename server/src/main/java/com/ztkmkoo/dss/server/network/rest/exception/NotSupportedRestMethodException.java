package com.ztkmkoo.dss.server.network.rest.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오후 11:27
 */
public class NotSupportedRestMethodException extends RuntimeException {

    private static final long serialVersionUID = 94339392028479793L;

    public NotSupportedRestMethodException() {
        super();
    }

    public NotSupportedRestMethodException(String message) {
        super(message);
    }

    public NotSupportedRestMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedRestMethodException(Throwable cause) {
        super(cause);
    }
}
