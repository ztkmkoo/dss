package io.github.ztkmkoo.dss.core.exception.network;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-01 02:38
 */
public class DssNetworkChannelException extends RuntimeException {
    private static final long serialVersionUID = -4161093301290554348L;

    public DssNetworkChannelException() {
    }

    public DssNetworkChannelException(String message) {
        super(message);
    }

    public DssNetworkChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssNetworkChannelException(Throwable cause) {
        super(cause);
    }

    public DssNetworkChannelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
