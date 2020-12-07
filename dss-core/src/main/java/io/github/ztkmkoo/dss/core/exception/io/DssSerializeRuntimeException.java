package io.github.ztkmkoo.dss.core.exception.io;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:40
 */
public class DssSerializeRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6748105471829252618L;

    public DssSerializeRuntimeException() {
    }

    public DssSerializeRuntimeException(String message) {
        super(message);
    }

    public DssSerializeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssSerializeRuntimeException(Throwable cause) {
        super(cause);
    }
}
