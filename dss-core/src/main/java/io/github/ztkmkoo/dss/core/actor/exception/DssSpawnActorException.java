package io.github.ztkmkoo.dss.core.actor.exception;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 03:22
 */
public class DssSpawnActorException extends RuntimeException {
    private static final long serialVersionUID = -8352074748356992794L;

    public DssSpawnActorException() {
    }

    public DssSpawnActorException(String message) {
        super(message);
    }

    public DssSpawnActorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssSpawnActorException(Throwable cause) {
        super(cause);
    }

    public DssSpawnActorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
