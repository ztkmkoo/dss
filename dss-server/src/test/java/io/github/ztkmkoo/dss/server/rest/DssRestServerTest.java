package io.github.ztkmkoo.dss.server.rest;

import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
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
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssLogLevel;
import io.github.ztkmkoo.dss.core.actor.exception.DssRestRequestMappingException;
import io.github.ztkmkoo.dss.core.exception.annotation.ExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.annotation.ServiceExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.handler.DssExceptionHandler;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorFormDataService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Timeout;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 8. 오후 4:59
 */
class DssRestServerTest {

    private static final String SSL_PASSWORD = "dss123";

    private static void startOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void stopDssRestServerAfterActivated(DssRestServer dssRestServer, int waitStartupSeconds, int waitShutdownSeconds) {
        startOnNewThread(() -> {
            try {
                await()
                        .atMost(waitStartupSeconds, TimeUnit.SECONDS)
                        .until(dssRestServer::isActivated);
                dssRestServer.stop();

                await()
                        .atMost(waitShutdownSeconds, TimeUnit.SECONDS)
                        .until(dssRestServer::isShutdown);
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
        final ClassLoader classLoader = DssRestServerTest.class.getClassLoader();
        return new File(URLDecoder.decode(classLoader.getResource(path).getFile(),"UTF-8"));
    }

    private static void startOnDssJsonRestServer(DssRestServer dssRestServer) {
        startOnNewThread(() -> {
            try {
                dssRestServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static HttpResponse sendJsonRequest(String uri, DssRestMethodType dssRestMethodType, String sendJsonBody) throws IOException {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final StringEntity params = new StringEntity(sendJsonBody);

        final HttpUriRequest request = RequestBuilder.create(dssRestMethodType.name())
                .setUri(uri)
                .addHeader("content-type", "application/json")
                .setCharset(StandardCharsets.UTF_8)
                .setEntity(params)
                .build();

        return httpClient.execute(request);
    }

    @Test
    void start() throws Exception {

        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new DssRestActorJsonService("test", "/hi", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                        return null;
                    }
                })
                .addDssService(new DssRestActorFormDataService("test2", "/hello", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Object>> request) {
                        return null;
                    }
                });

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    void test() throws InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new TestService("test", "/hi", DssRestMethodType.GET));

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    void testSsl() throws Exception {
        final PrivateKey privateKey = loadPrivateKeyFromFile(loadFromTestResources("ssl/private.der"));
        final X509Certificate certificate = loadX509CertificateFromFile(loadFromTestResources("ssl/private.crt"));

        final SslContext sslContext = SslContextBuilder.forServer(privateKey, SSL_PASSWORD, certificate).build();

        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181, DssLogLevel.DEBUG, true, sslContext);
        dssRestServer
                .addDssService(new TestService("test", "/hi", DssRestMethodType.GET));

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    void testStartExceptionHandler() throws InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new TestService("test", "/hi", DssRestMethodType.GET))
                .addExceptionHandler(new GlobalExceptionHandler());

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    @Timeout(value = 15)
    void testJsonPostRequest() throws IOException, InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new TestJsonService("test_json", "/test/json", DssRestMethodType.POST));

        startOnDssJsonRestServer(dssRestServer);
        await().until(dssRestServer::isActivated);

        final String uri = "http://127.0.0.1:8181/test/json";
        final String sendJsonBody = "{\"name\":\"myname\",\"age\":20}";

        HttpResponse response = sendJsonRequest(uri, DssRestMethodType.POST, sendJsonBody);

        String receiveJsonBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        assertEquals(sendJsonBody, receiveJsonBody);

        dssRestServer.stop();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    @Timeout(value = 15)
    void globalExceptionHandleMethodTest() throws IOException, InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new MyService())
                .addDssService(new YourService())
                .addExceptionHandler(new GlobalExceptionHandler());

        startOnDssJsonRestServer(dssRestServer);
        await().until(dssRestServer::isActivated);

        String uri = "http://127.0.0.1:8181/your/service";
        String sendJsonBody = "{\"name\":\"myname\"}";

        HttpResponse response = sendJsonRequest(uri, DssRestMethodType.POST, sendJsonBody);

        String receiveJsonBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        assertEquals("{\"result\":\"globalExceptionHandleMethod\"}", receiveJsonBody);

        dssRestServer.stop();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    @Timeout(value = 15)
    void serviceExceptionHandleMethodTest() throws IOException, InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new MyService())
                .addDssService(new YourService())
                .addExceptionHandler(new GlobalExceptionHandler());

        startOnDssJsonRestServer(dssRestServer);
        await().until(dssRestServer::isActivated);

        final String uri = "http://127.0.0.1:8181/my/service";
        final String sendJsonBody = "{\"name\":\"myname\"}";

        HttpResponse response = sendJsonRequest(uri, DssRestMethodType.POST, sendJsonBody);

        String receiveJsonBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        assertEquals("{\"result\":\"serviceExceptionHandleMethod\"}", receiveJsonBody);

        dssRestServer.stop();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    @Timeout(value = 15)
    void occurExceptionTestWhileRunningExceptionHandleMethod() throws IOException, InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssService(new MyService())
                .addDssService(new YourService())
                .addExceptionHandler(new GlobalExceptionHandler());

        startOnDssJsonRestServer(dssRestServer);
        await().until(dssRestServer::isActivated);

        final String sendJsonBody = "{\"name\":\"myname\",\"age\":20}";
        final String uri = "http://127.0.0.1:8181/my/service";

        HttpResponse response = sendJsonRequest(uri, DssRestMethodType.POST, sendJsonBody);

        String receiveJsonBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        assertEquals("", receiveJsonBody);

        dssRestServer.stop();

        assertTrue(dssRestServer.isShutdown());
    }

    @Getter
    private static class TestResponse implements DssRestServiceResponse {
        private static final long serialVersionUID = 5967168992972178660L;

        private final String message;

        public TestResponse(String name) {
            this.message = "Hi " + name;
        }
    }

    @Getter @Setter
    private static class TestRequest implements Serializable {
        private static final long serialVersionUID = 6373259023479826730L;

        private String name;
    }

    private static class TestService extends DssRestActorJsonService<TestRequest> {

        public TestService(String name, String path, DssRestMethodType methodType) {
            super(new TypeReference<TestRequest>() {}, name, path, methodType);
        }

        @Override
        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestRequest> request) {
            final TestRequest testRequest = request.getBody();
            final String name = testRequest.getName();
            return new TestResponse(name);
        }
    }

    @Getter
    private static class TestJsonResponse implements DssRestServiceResponse {
        private static final long serialVersionUID = 5199731949724988897L;

        private final String name;
        private final int age;

        public TestJsonResponse(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Getter @Setter
    private static class TestJsonRequest implements Serializable {
        private static final long serialVersionUID = 2415190317023826555L;

        private String name;
        private int age;
    }

    private static class TestJsonService extends DssRestActorJsonService<TestJsonRequest> {

        public TestJsonService(String name, String path, DssRestMethodType methodType) {
            super(new TypeReference<TestJsonRequest>() {}, name, path, methodType);
        }

        @Override
        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestJsonRequest> request) {
            final TestJsonRequest testRequest = request.getBody();

            return new TestJsonResponse(testRequest.name, testRequest.age);
        }
    }

    private static class MyService extends DssRestActorJsonService<TestRequest> {
        public MyService() {
            super(new TypeReference<TestRequest>() {}, "myService", "/my/service", DssRestMethodType.POST);
        }

        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestRequest> dssRestServiceRequest) {
            throw new NullPointerException();
        }
    }

    private static class YourService extends DssRestActorJsonService<TestRequest> {
        public YourService() {
            super(new TypeReference<TestRequest>() {}, "yourService", "/your/service", DssRestMethodType.POST);
        }

        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestRequest> dssRestServiceRequest) {
            throw new NullPointerException();
        }
    }

    private static class GlobalExceptionHandler implements DssExceptionHandler {

        @ExceptionHandler(exception = NullPointerException.class)
        public DssRestServiceResponse globalExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
            return new globalResponse("globalExceptionHandleMethod");
        }

        @ExceptionHandler(exception = DssRestRequestMappingException.class)
        public DssRestServiceResponse testForExceptionWhileRunningExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
            throw new RuntimeException("testForExceptionWhileRunningExceptionHandleMethod");
        }

        @ServiceExceptionHandler(service = MyService.class, exception = NullPointerException.class)
        public DssRestServiceResponse serviceExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
            return new globalResponse("serviceExceptionHandleMethod");
        }

        @Getter @Setter
        static class globalResponse implements DssRestServiceResponse {
            private static final long serialVersionUID = -8811453587826373582L;
            private String result;

            public globalResponse(String result) {
                this.result = result;
            }
        }
    }
}