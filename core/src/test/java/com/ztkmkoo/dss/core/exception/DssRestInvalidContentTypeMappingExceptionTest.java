package com.ztkmkoo.dss.core.exception;

import org.junit.Test;

import java.io.IOException;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:35
 */
public class DssRestInvalidContentTypeMappingExceptionTest {

    @Test(expected = DssRestInvalidContentTypeMappingException.class)
    public void testException() {
        throw new DssRestInvalidContentTypeMappingException("test");
    }

    @Test(expected = DssRestInvalidContentTypeMappingException.class)
    public void testException2() {
        throw new DssRestInvalidContentTypeMappingException("test", new NullPointerException("why not null?"));
    }

    @Test(expected = DssRestInvalidContentTypeMappingException.class)
    public void testException3() {
        throw new DssRestInvalidContentTypeMappingException(new IOException());
    }
}