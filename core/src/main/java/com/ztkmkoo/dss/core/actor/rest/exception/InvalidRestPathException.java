package com.ztkmkoo.dss.core.actor.rest.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 9:58
 */
public class InvalidRestPathException extends RuntimeException {

    public InvalidRestPathException() {
    }

    public InvalidRestPathException(String message) {
        super(message);
    }

    public InvalidRestPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRestPathException(Throwable cause) {
        super(cause);
    }
}
