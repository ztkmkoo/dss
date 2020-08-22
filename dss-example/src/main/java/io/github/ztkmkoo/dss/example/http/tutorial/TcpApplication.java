package io.github.ztkmkoo.dss.example.http.tutorial;

import io.github.ztkmkoo.dss.server.tcp.DssTcpServer;

public class TcpApplication {
    public static void main(String[] args) throws InterruptedException {
        DssTcpServer dssTcpServer = new DssTcpServer("127.0.0.1", 8181);

        dssTcpServer.start();
    }
}
