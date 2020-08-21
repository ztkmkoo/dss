package io.github.ztkmkoo.dss.core.message.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:29
 */
class DssRestChannelInitializerCommandHandlerUnregisteredTest {

    private final DssRestChannelInitializerCommandHandlerUnregistered command = DssRestChannelInitializerCommandHandlerUnregistered
            .builder()
            .name("hi")
            .handlerActor(null)
            .build();
    @Test
    void getName() {
        assertEquals("hi", command.getName());
    }

    @Test
    void getHandlerActor() {
        assertNull(command.getHandlerActor());
    }

    @Test
    void testToString() {
        assertNotNull(command.toString());
    }
}