package io.github.ztkmkoo.dss.core.exception;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:35
 */
class DssRestInvalidContentTypeMappingExceptionTest {

    @Test
    void testException() {
        assertThrows(DssRestInvalidContentTypeMappingException.class, () -> {throw new DssRestInvalidContentTypeMappingException("test");});
    }

    @Test
    void testException2() {
        final NullPointerException e = new NullPointerException("why not null?");
        assertThrows(DssRestInvalidContentTypeMappingException.class, () -> {
            throw new DssRestInvalidContentTypeMappingException("test", e);
        });
    }

    @Test
    void testException3() {
        final IOException e = new IOException();
        assertThrows(DssRestInvalidContentTypeMappingException.class, () -> {
            throw new DssRestInvalidContentTypeMappingException(e);
        });
    }
}