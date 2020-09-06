package io.github.ztkmkoo.dss.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:14
 */
class ObjectUtilsTest {

    @Test
    void defaultValueIfNull() {
        final String result = ObjectUtils.defaultValueIfNull("Hi", "Hello", "greeting");
        assertEquals("Hi", result);
    }

    @Test
    void defaultValueIfNull2() {
        final String result = ObjectUtils.defaultValueIfNull(null, "Hello", "greeting");
        assertEquals("Hello", result);
    }
}