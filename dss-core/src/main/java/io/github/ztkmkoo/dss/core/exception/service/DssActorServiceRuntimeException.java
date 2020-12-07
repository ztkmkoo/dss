package io.github.ztkmkoo.dss.core.exception.service;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 02:58
 */
public class DssActorServiceRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 948582052658451080L;

    public DssActorServiceRuntimeException() {
    }

    public DssActorServiceRuntimeException(String message) {
        super(message);
    }
}
