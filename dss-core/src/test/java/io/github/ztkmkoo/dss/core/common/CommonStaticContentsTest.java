package io.github.ztkmkoo.dss.core.common;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:37
 */
class CommonStaticContentsTest {

    @Test
    void getDefaultHost() {
        assertEquals("0.0.0.0", CommonStaticContents.getDefaultHost());
    }

    @Test
    void getDefaultPort() {
        assertEquals(8080, CommonStaticContents.getDefaultPort());
    }

    @Test
    void getDefaultLogLevel() {
        assertEquals(DssLogLevel.DEBUG, CommonStaticContents.getDefaultLogLevel());
    }

    @Test
    void getTextHost() {
        assertEquals("host", CommonStaticContents.getTextHost());
    }

    @Test
    void getTextPort() {
        assertEquals("port", CommonStaticContents.getTextPort());
    }

    @Test
    void getTextLogLevel() {
        assertEquals("logLevel", CommonStaticContents.getTextLogLevel());
    }
}