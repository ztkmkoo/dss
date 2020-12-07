package io.github.ztkmkoo.dss.core.exception.io;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:40
 */
public class DssDeserializeRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6748105471829252618L;

    public DssDeserializeRuntimeException() {
    }

    public DssDeserializeRuntimeException(String message) {
        super(message);
    }

    public DssDeserializeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssDeserializeRuntimeException(Throwable cause) {
        super(cause);
    }
}
