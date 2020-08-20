package io.github.ztkmkoo.dss.server.tcp;

import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class DssTcpServerTest {

    @Test
    void start() throws InterruptedException {

        final DssTcpServer dssTcpServer = new DssTcpServer("127.0.0.1", 8181);

        stopDssTcpServerAfterActivated(dssTcpServer, 10, 15);

        dssTcpServer.start();

        assertTrue(dssTcpServer.isShutdown());
    }

    private static void startOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void stopDssTcpServerAfterActivated(DssTcpServer dssTcpServer, int waitStartupSeconds, int waitShutdownSeconds) {
        startOnNewThread(() -> {
            await()
                .atMost(waitStartupSeconds, TimeUnit.SECONDS)
                .until(dssTcpServer::isActivated);
            dssTcpServer.stop();

            await()
                .atMost(waitShutdownSeconds, TimeUnit.SECONDS)
                .until(dssTcpServer::isShutdown);
        });
    }
}