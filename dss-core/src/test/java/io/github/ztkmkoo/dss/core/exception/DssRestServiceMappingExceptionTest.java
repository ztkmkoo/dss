package io.github.ztkmkoo.dss.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:33
 */
public class DssRestServiceMappingExceptionTest {

    @Test
    public void testException() {
        assertThrows(DssRestServiceMappingException.class, () -> {
            throw new DssRestServiceMappingException("test");
        });
    }

    @Test
    public void testException2() {
        assertThrows(DssRestServiceMappingException.class, () -> {
            throw new DssRestServiceMappingException("test", new NullPointerException("why not null?"));
        });
    }

    @Test
    public void testException3() {
        assertThrows(DssRestServiceMappingException.class, () -> {
            throw new DssRestServiceMappingException(new IOException());
        });
    }
}