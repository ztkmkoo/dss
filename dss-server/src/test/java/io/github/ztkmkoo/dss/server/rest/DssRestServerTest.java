package io.github.ztkmkoo.dss.server.rest;

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
import org.junit.Test;

import java.io.*;
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

import static org.awaitility.Awaitility.*;
import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 8. 오후 4:59
 */
public class DssRestServerTest {

    private static final String SSL_PASSWORD = "dss123";

    @Test
    public void start() throws Exception {

        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssRestService(new DssRestActorJsonService("test", "/hi", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                        return null;
                    }
                })
                .addDssRestService(new DssRestActorFormDataService("test2", "/hello", DssRestMethodType.GET) {
                    @Override
                    protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<HashMap<String, Serializable>> request) {
                        return null;
                    }
                });

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    public void test() throws InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssRestService(new TestService("test", "/hi", DssRestMethodType.GET));

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    public void testSsl() throws Exception {
        final PrivateKey privateKey = loadPrivateKeyFromFile(loadFromTestResources("ssl/private.der"));
        final X509Certificate certificate = loadX509CertificateFromFile(loadFromTestResources("ssl/private.crt"));

        final SslContext sslContext = SslContextBuilder.forServer(privateKey, SSL_PASSWORD, certificate).build();

        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181, true, sslContext);
        dssRestServer
                .addDssRestService(new TestService("test", "/hi", DssRestMethodType.GET));

        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Test
    public void testFormData() throws InterruptedException {
        final DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);
        dssRestServer
                .addDssRestService(new TestFormDataService("test", "/hi", DssRestMethodType.GET))
                .addDssRestService(new TestFormDataService("test2", "/hello", DssRestMethodType.POST));

//        stopDssRestServerAfterActivated(dssRestServer, 10 ,15);

        dssRestServer.start();

        assertTrue(dssRestServer.isShutdown());
    }

    @Getter @Setter
    private static class TestRequest implements Serializable {
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

    private static File loadFromTestResources(String path) {
        final ClassLoader classLoader = DssRestServerTest.class.getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}