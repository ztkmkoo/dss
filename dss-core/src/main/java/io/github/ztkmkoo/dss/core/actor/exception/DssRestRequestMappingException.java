package io.github.ztkmkoo.dss.core.actor.exception;

public class DssRestRequestMappingException extends RuntimeException{
    private static final long serialVersionUID = 6545592855014233698L;

    public DssRestRequestMappingException(String message) {
        super(message);
    }

    public DssRestRequestMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssRestRequestMappingException(Throwable cause) {
        super(cause);
    }
}
