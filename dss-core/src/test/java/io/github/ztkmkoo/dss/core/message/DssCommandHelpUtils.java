package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:59
 */
public class DssCommandHelpUtils {

    private DssCommandHelpUtils() {}

    public static void assertDefaultBindCommand(DssBindCommand command) {
        assertNotNull(command);

        assertEquals("0.0.0.0", command.getHost());
        assertEquals(8080, command.getPort());
        assertEquals(DssLogLevel.DEBUG, command.getLogLevel());
    }

    public static void assertBindCommand(DssBindCommand command, String expectedHost, int expectedPort, DssLogLevel expectedLogLevel) {
        assertNotNull(command);

        assertEquals(expectedHost, command.getHost());
        assertEquals(expectedPort, command.getPort());
        assertEquals(expectedLogLevel, command.getLogLevel());
    }
}
