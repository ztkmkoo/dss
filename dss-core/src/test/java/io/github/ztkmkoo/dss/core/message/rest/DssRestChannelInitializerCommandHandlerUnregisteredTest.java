package io.github.ztkmkoo.dss.core.message.rest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 6. 오전 1:29
 */
public class DssRestChannelInitializerCommandHandlerUnregisteredTest {

    private final DssRestChannelInitializerCommandHandlerUnregistered command = DssRestChannelInitializerCommandHandlerUnregistered
            .builder()
            .name("hi")
            .handlerActor(null)
            .build();
    @Test
    public void getName() {
        assertEquals("hi", command.getName());
    }

    @Test
    public void getHandlerActor() {
        assertNull(command.getHandlerActor());
    }

    @Test
    public void testToString() {
        assertNotNull(command.toString());
    }
}