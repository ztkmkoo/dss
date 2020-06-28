package io.github.ztkmkoo.dss.core.network.exception;

public class NotValidDssPropertyParameter extends RuntimeException {

    public NotValidDssPropertyParameter() {
    }

    public NotValidDssPropertyParameter(String message) {
        super(message);
    }

    public NotValidDssPropertyParameter(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidDssPropertyParameter(Throwable cause) {
        super(cause);
    }

    public NotValidDssPropertyParameter(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
