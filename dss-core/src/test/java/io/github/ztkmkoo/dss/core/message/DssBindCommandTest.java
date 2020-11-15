package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-15 23:07
 */
class DssBindCommandTest {

    @Test
    void test() {
        final DssBindCommand command = new TestBuilder()
                .logLevel(DssLogLevel.DEBUG)
                .port(8181)
                .certificatePath("test/certPath")
                .host("localhost")
                .password("pswd")
                .privateKeyPath(null)
                .ssl(true)
                .build();

        assertNotNull(command);
        assertEquals(DssLogLevel.DEBUG, command.getLogLevel());
        assertEquals(8181, command.getPort());
        assertFalse(command.getCertificatePath().isEmpty());
        assertEquals("localhost", command.getHost());
        assertNull(command.getPrivateKeyPath());
        assertTrue(command.isSsl());
    }

    static class TestBuilder extends DssBindCommand.Builder<DssBindCommand> {

        @Override
        public DssBindCommand build() {
            return new DssBindCommand(this);
        }
    }
}