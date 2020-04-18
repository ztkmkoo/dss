package com.ztkmkoo.dss.core.actor.rest.util;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 18. 오전 4:36
 */
public class DssRestPathUtilsTest {

    private final String path = "/test?id=z";

    @Test
    public void getFixedPath() {
        final String fixedPath = DssRestPathUtils.getFixedPath(path);

        assertEquals("/test", fixedPath);
    }

    @Test
    public void getRequestParamString() {
        final String paramString = DssRestPathUtils.getRequestParamString(path);

        assertEquals("id=z", paramString);
    }

    @Test
    public void containRequestParam() {
        assertTrue(DssRestPathUtils.containRequestParam(path));
    }

    @Test
    public void getRequestParams() {
        final String paramString = DssRestPathUtils.getRequestParamString(path);
        final Map<String, String> param = DssRestPathUtils.getRequestParams(paramString);

        assertNotNull(param);
        assertEquals(1, param.size());
        assertEquals("z", param.get("id"));
    }
}