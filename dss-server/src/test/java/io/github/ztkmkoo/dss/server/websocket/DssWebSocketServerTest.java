package io.github.ztkmkoo.dss.server.websocket;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssLogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class DssWebSocketServerTest {

    private static final String SSL_PASSWORD = "dss123";

    @Test
    void start() throws Exception {

        final DssWebSocketServer dssWebsocketServer = new DssWebSocketServer("127.0.0.1", 8181);

        stopDssWebsocketServerAfterActivated(dssWebsocketServer, 10, 15);

        dssWebsocketServer.start();

        assertTrue(dssWebsocketServer.isShutdown());
    }

    private static void startOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Test
    void testSsl() throws Exception {
        final PrivateKey privateKey = loadPrivateKeyFromFile(loadFromTestResources("ssl/private.der"));
        final X509Certificate certificate = loadX509CertificateFromFile(loadFromTestResources("ssl/private.crt"));

        final SslContext sslContext = SslContextBuilder.forServer(privateKey, SSL_PASSWORD, certificate).build();

        final DssWebSocketServer dssWebsocketServer = new DssWebSocketServer("127.0.0.1", 8181,
                DssLogLevel.DEBUG, true, sslContext);

        stopDssWebsocketServerAfterActivated(dssWebsocketServer, 10, 15);

        dssWebsocketServer.start();

        assertTrue(dssWebsocketServer.isShutdown());

    }

    private static void stopDssWebsocketServerAfterActivated(DssWebSocketServer dssWebsocketServer, int waitStartupSeconds, int waitShutdownSeconds) {
        startOnNewThread(() -> {
            try {
                await()
                        .atMost(waitStartupSeconds, TimeUnit.SECONDS)
                        .until(dssWebsocketServer::isActivated);
                dssWebsocketServer.stop();

                await()
                        .atMost(waitShutdownSeconds, TimeUnit.SECONDS)
                        .until(dssWebsocketServer::isShutdown);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static PrivateKey loadPrivateKeyFromFile(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final FileInputStream fis = new FileInputStream(file);
        final byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        final KeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private static X509Certificate loadX509CertificateFromFile(File file) throws IOException, CertificateException {
        final FileInputStream fis = new FileInputStream(file);
        final byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

        final CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate)factory.generateCertificate(bais);
    }

    private static File loadFromTestResources(String path) throws UnsupportedEncodingException {
        final ClassLoader classLoader = DssWebSocketServer.class.getClassLoader();
        return new File(URLDecoder.decode(classLoader.getResource(path).getFile(),"UTF-8"));
    }
}