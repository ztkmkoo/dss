package io.github.ztkmkoo.dss.core.actor.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 1:02
 */
public class DssUserActorDuplicateBehaviorCreateException extends RuntimeException {

    private static final long serialVersionUID = 4627936611243117800L;

    public DssUserActorDuplicateBehaviorCreateException(String message) {
        super(message);
    }

    public DssUserActorDuplicateBehaviorCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DssUserActorDuplicateBehaviorCreateException(Throwable cause) {
        super(cause);
    }
}
