package io.github.ztkmkoo.dss.core.message.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 5. 오전 12:42
 */
class DssRestChannelHandlerCommandResponseTest {

    @Test
    void testToString() {
        final DssRestChannelHandlerCommandResponse response = DssRestChannelHandlerCommandResponse.builder().channelId("Hello").build();
        assertFalse(response.toString().isEmpty());
    }

    @Test
    void getChannelId() {
        final DssRestChannelHandlerCommandResponse response = DssRestChannelHandlerCommandResponse.builder().channelId("Hello").build();
        assertEquals("Hello", response.getChannelId());
    }
}