package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:45
 */
class DssNetworkCommandTest {

    @Test
    void bind() {
        final DssNetworkCommand.Bind command = DssNetworkCommand.Bind
                .builder()
                .host("127.0.0.1")
                .port(433)
                .logLevel(DssLogLevel.WARN)
                .build();

        DssCommandHelpUtils.assertBindCommand(command, "127.0.0.1", 433, DssLogLevel.WARN);
    }

    @Test
    void bindDefault() {
        final DssNetworkCommand.Bind defaultCommand = DssNetworkCommand.Bind.builder().build();
        DssCommandHelpUtils.assertDefaultBindCommand(defaultCommand);
    }
}