package io.github.ztkmkoo.dss.server.websocket;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssLogLevel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DssWebSocketServerTest {

    private static final String SSL_PASSWORD = "dss123";

    private static void startOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void stopDssWebSocketServerAfterActivated(DssWebSocketServer dssWebSocketServer, int waitStartupSeconds, int waitShutdownSeconds) {
        startOnNewThread(() -> {
            try {
                await()
                        .atMost(waitStartupSeconds, TimeUnit.SECONDS)
                        .until(dssWebSocketServer::isActivated);
                dssWebSocketServer.stop();

                await()
                        .atMost(waitShutdownSeconds, TimeUnit.SECONDS)
                        .until(dssWebSocketServer::isShutdown);
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
        final ClassLoader classLoader = DssWebSocketServerTest.class.getClassLoader();
        return new File(URLDecoder.decode(classLoader.getResource(path).getFile(),"UTF-8"));
    }

    private static void startOnDssTextWebSocketServer(DssWebSocketServer dssWebSocketServer) {
        startOnNewThread(() -> {
            try {
                dssWebSocketServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static HttpResponse sendTextRequest(String uri, HttpMethod method, String sendTextBody) throws IOException {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final StringEntity params = new StringEntity(sendTextBody);

        final HttpUriRequest request = RequestBuilder.create(String.valueOf(method))
                .setUri(uri)
                .addHeader("content-type", "text/html")
                .setCharset(StandardCharsets.UTF_8)
                .setEntity(params)
                .build();

        return httpClient.execute(request);
    }

    @Test
    void start() throws InterruptedException {

        final DssWebSocketServer dssWebSocketServer = new DssWebSocketServer("127.0.0.1", 8181);

        stopDssWebSocketServerAfterActivated(dssWebSocketServer, 10, 15);

        dssWebSocketServer.start();

        assertTrue(dssWebSocketServer.isShutdown());
    }

    @Test
    void test() throws InterruptedException {
        final DssWebSocketServer dssWebSocketServer = new DssWebSocketServer("127.0.0.1", 8181);
        stopDssWebSocketServerAfterActivated(dssWebSocketServer, 10 ,15);

        dssWebSocketServer.start();

        assertTrue(dssWebSocketServer.isShutdown());
    }

    @Test
    void testSsl() throws Exception {
        final PrivateKey privateKey = loadPrivateKeyFromFile(loadFromTestResources("ssl/private.der"));
        final X509Certificate certificate = loadX509CertificateFromFile(loadFromTestResources("ssl/private.crt"));

        final SslContext sslContext = SslContextBuilder.forServer(privateKey, SSL_PASSWORD, certificate).build();

        final DssWebSocketServer dssWebSocketServer = new DssWebSocketServer("127.0.0.1", 8181, DssLogLevel.INFO, true, sslContext);
        stopDssWebSocketServerAfterActivated(dssWebSocketServer, 10 ,15);

        dssWebSocketServer.start();

        assertTrue(dssWebSocketServer.isShutdown());
    }

    @Test
    @Timeout(value = 15)
    void testGetRequest() throws IOException, InterruptedException {
        final DssWebSocketServer dssWebSocketServer = new DssWebSocketServer("127.0.0.1", 8181);

        startOnDssTextWebSocketServer(dssWebSocketServer);
        await().until(dssWebSocketServer::isActivated);

        final String uri = "http://127.0.0.1:8181";
        final String sendTextBody = "Hello";

        HttpResponse response = sendTextRequest(uri, HttpMethod.GET, sendTextBody);

        String receiveTextBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        assertEquals(sendTextBody, receiveTextBody);

        dssWebSocketServer.stop();

        assertTrue(dssWebSocketServer.isShutdown());
    }

    @Test
    @Timeout(value = 15)
    void testWebsocketGetRequest() throws Exception {
        final DssWebSocketServer dssWebSocketServer = new DssWebSocketServer("127.0.0.1", 8181);

        startOnDssTextWebSocketServer(dssWebSocketServer);
        await().until(dssWebSocketServer::isActivated);

        final URI uri = URI.create("ws://127.0.0.1:8181/websocket");

        WebSocketClient client = new WebSocketClient();
        MyWebSocket socket = new MyWebSocket();

        client.start();

        ClientUpgradeRequest request = new ClientUpgradeRequest();
        Future<Session> future = client.connect(socket, uri, request);
        Session session = future.get();
        UpgradeResponse response = session.getUpgradeResponse();
        assertEquals("Upgrade", response.getHeader("Connection"));
        assertEquals("websocket", response.getHeader("Upgrade"));
        assertEquals("Switching Protocols", response.getStatusReason());
        assertEquals(101, response.getStatusCode());

        session.close();

        dssWebSocketServer.stop();

        assertTrue(dssWebSocketServer.isShutdown());
    }

    @WebSocket
    public class MyWebSocket {

        @OnWebSocketConnect
        public void onConnect(Session session) throws IOException {
            session.getRemote().sendString("Hello server");
        }

        @OnWebSocketMessage
        public void onMessage(String message) {
            System.out.println("Message from Server: " + message);
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            System.out.println("WebSocket Closed. Code:" + statusCode);
        }

    }
}
