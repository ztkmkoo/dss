package io.github.ztkmkoo.dss.core.common.security;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-22 07:56
 */
public class SslContextUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SslContextUtils.class);

    private SslContextUtils() {}

    public static SslContext sslContextForServer(String privateKeyPath, String certificatePath) {
        return sslContextForServer(privateKeyPath, certificatePath, null);
    }

    public static SslContext sslContextForServer(String privateKeyPath, String certificatePath, String password) {
        try {
            final PrivateKey privateKey = loadPrivateKeyFromFile(new File(privateKeyPath));
            final X509Certificate certificate = loadX509CertificateFromFile(new File(certificatePath));

            if (Objects.nonNull(password)) {
                return SslContextBuilder.forServer(privateKey, password, certificate).build();
            } else {
                return SslContextBuilder.forServer(privateKey, certificate).build();
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            return null;
        }
    }

    private static PrivateKey loadPrivateKeyFromFile(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (final FileInputStream fis = new FileInputStream(file)){
            final byte[] buffer = new byte[fis.available()];
            final int read = fis.read(buffer);
            LOGGER.debug("loadPrivateKeyFromFile {} bytes", read);

            final KeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    private static X509Certificate loadX509CertificateFromFile(File file) throws IOException, CertificateException {
        try (final FileInputStream fis = new FileInputStream(file)){
            final byte[] buffer = new byte[fis.available()];
            final int read = fis.read(buffer);
            LOGGER.debug("loadX509CertificateFromFile {} bytes", read);

            final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

            final CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate)factory.generateCertificate(bais);
        }
    }
}
