package io.github.ztkmkoo.dss.core.common.security;

import io.netty.handler.ssl.SslContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-15 22:53
 */
class SslContextUtilsTest {

    private static File loadFromTestResources(String path) throws UnsupportedEncodingException {
        final ClassLoader classLoader = SslContextUtilsTest.class.getClassLoader();
        final URL url = classLoader.getResource(path);
        Objects.requireNonNull(url);

        return new File(URLDecoder.decode(url.getFile(),"UTF-8"));
    }

    @Test
    void sslContextForServer() throws UnsupportedEncodingException {
        final File keyFile = loadFromTestResources("ssl/private.der");
        final File certFile = loadFromTestResources("ssl/private.crt");

        final SslContext sslContext = SslContextUtils.sslContextForServer(keyFile.getPath(), certFile.getPath());
        assertNotNull(sslContext);
        assertTrue(sslContext.isServer());
    }
}