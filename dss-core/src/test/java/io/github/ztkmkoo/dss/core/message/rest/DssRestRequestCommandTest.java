package io.github.ztkmkoo.dss.core.message.rest;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-19 01:16
 */
class DssRestRequestCommandTest {

    private static final String DEFAULT_CHANNEL_ID = "test channel id";

    private final DssRestRequestCommand.Builder builder = DssRestRequestCommand.builder(DEFAULT_CHANNEL_ID, null);

    @Test
    void getChannelId() {
        final DssRestRequestCommand cmd = builder.build();
        assertEquals(DEFAULT_CHANNEL_ID, cmd.getChannelId());
    }

    @Test
    void getSender() {
        final DssRestRequestCommand cmd = builder.build();
        assertNull(cmd.getSender());
    }

    @Test
    void defaultValue() {
        final DssRestRequestCommand cmd = builder.build();

        assertEquals(DssRestMethodType.GET, cmd.getMethodType());
        assertEquals(DssRestContentType.APPLICATION_JSON, cmd.getContentType());
        assertEquals("UTF-8", cmd.getCharset());
        assertEquals("", cmd.getPath());
        assertEquals(0, cmd.getContent().length);
    }

    @Test
    void build() throws UnsupportedEncodingException {
        final String charset = "UTF-8";

        final DssRestRequestCommand cmd = builder
                .methodType(DssRestMethodType.POST)
                .contentType(DssRestContentType.APPLICATION_WWW_FORM_URL_ENCODED)
                .charset(charset)
                .path("/test")
                .content("Hello".getBytes(charset))
                .build();

        assertEquals(DssRestMethodType.POST, cmd.getMethodType());
        assertEquals(DssRestContentType.APPLICATION_WWW_FORM_URL_ENCODED, cmd.getContentType());
        assertEquals(charset, cmd.getCharset());
        assertEquals("/test", cmd.getPath());

        final byte[] expectedBytes = new byte[5];
        expectedBytes[0] = 72;
        expectedBytes[1] = 101;
        expectedBytes[2] = 108;
        expectedBytes[3] = 108;
        expectedBytes[4] = 111;
        assertArrayEquals(expectedBytes, cmd.getContent());
    }
}