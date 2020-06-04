package io.github.ztkmkoo.dss.core.network.rest.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:45
 */
public class NotSupportedRestMethodException extends RuntimeException {

    private static final long serialVersionUID = 3588054506212254573L;

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
