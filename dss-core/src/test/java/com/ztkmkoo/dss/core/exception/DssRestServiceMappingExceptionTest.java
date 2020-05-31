package com.ztkmkoo.dss.core.exception;

import org.junit.Test;

import java.io.IOException;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:33
 */
public class DssRestServiceMappingExceptionTest {

    @Test(expected = DssRestServiceMappingException.class)
    public void testException() {
        throw new DssRestServiceMappingException("test");
    }

    @Test(expected = DssRestServiceMappingException.class)
    public void testException2() {
        throw new DssRestServiceMappingException("test", new NullPointerException("why not null?"));
    }

    @Test(expected = DssRestServiceMappingException.class)
    public void testException3() {
        throw new DssRestServiceMappingException(new IOException());
    }
}