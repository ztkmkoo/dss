package io.github.ztkmkoo.dss.core.network.rest.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 1:00
 */
public class NotSupportedRestMethodExceptionTest {

    @Test
    public void constructor() {
        final NotSupportedRestMethodException exception = new NotSupportedRestMethodException("no");
        assertEquals("no", exception.getMessage());
    }

    @Test
    public void constructor2() {
        final NotSupportedRestMethodException exception = new NotSupportedRestMethodException(new NullPointerException());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    @Test
    public void constructor3() {
        final NotSupportedRestMethodException exception = new NotSupportedRestMethodException("no", new Exception());
        assertEquals("no", exception.getMessage());
        assertEquals(Exception.class, exception.getCause().getClass());
    }
}