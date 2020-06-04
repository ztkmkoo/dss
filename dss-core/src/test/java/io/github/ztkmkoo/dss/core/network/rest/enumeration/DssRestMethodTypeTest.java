package io.github.ztkmkoo.dss.core.network.rest.enumeration;

import io.github.ztkmkoo.dss.core.network.rest.exception.NotSupportedRestMethodException;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:57
 */
public class DssRestMethodTypeTest {

    @Test
    public void fromNettyHttpMethod() {
        final DssRestMethodType methodType = DssRestMethodType.fromNettyHttpMethod(HttpMethod.GET);
        assertEquals(DssRestMethodType.GET, methodType);
    }

    @Test(expected = NotSupportedRestMethodException.class)
    public void fromNettyHttpMethodNotSupported() {
        DssRestMethodType.fromNettyHttpMethod(HttpMethod.HEAD);
    }

    @Test
    public void valueOf() {
        final DssRestMethodType methodType = DssRestMethodType.valueOf("GET");
        assertEquals(DssRestMethodType.GET, methodType);
    }
}