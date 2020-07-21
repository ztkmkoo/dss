package io.github.ztkmkoo.dss.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:35
 */
public class DssRestInvalidContentTypeMappingExceptionTest {

    @Test
    public void testException() {
        assertThrows(DssRestInvalidContentTypeMappingException.class, () -> {
            throw new DssRestInvalidContentTypeMappingException("test");
        });
    }

    @Test
    public void testException2() {
        assertThrows(DssRestInvalidContentTypeMappingException.class, () -> {
            throw new DssRestInvalidContentTypeMappingException("test", new NullPointerException("why not null?"));
        });
    }

    @Test
    public void testException3() {
        assertThrows(DssRestInvalidContentTypeMappingException.class, () -> {
            throw new DssRestInvalidContentTypeMappingException(new IOException());
        });
    }
}