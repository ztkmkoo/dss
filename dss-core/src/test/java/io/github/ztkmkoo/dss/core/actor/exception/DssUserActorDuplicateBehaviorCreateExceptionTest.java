package io.github.ztkmkoo.dss.core.actor.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:50
 */
public class DssUserActorDuplicateBehaviorCreateExceptionTest {

    @Test
    public void constructor() {
        final DssUserActorDuplicateBehaviorCreateException exception = new DssUserActorDuplicateBehaviorCreateException("no");
        assertEquals("no", exception.getMessage());
    }

    @Test
    public void constructor2() {
        final DssUserActorDuplicateBehaviorCreateException exception = new DssUserActorDuplicateBehaviorCreateException(new NullPointerException());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    @Test
    public void constructor3() {
        final DssUserActorDuplicateBehaviorCreateException exception = new DssUserActorDuplicateBehaviorCreateException("no", new Exception());
        assertEquals("no", exception.getMessage());
        assertEquals(Exception.class, exception.getCause().getClass());
    }
}