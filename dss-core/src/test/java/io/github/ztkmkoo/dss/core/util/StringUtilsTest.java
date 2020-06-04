package io.github.ztkmkoo.dss.core.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:37
 */
public class StringUtilsTest {

    @Test
    public void isEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(null));
        assertFalse(StringUtils.isEmpty("hi"));
    }
}