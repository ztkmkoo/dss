package io.github.ztkmkoo.dss.core.exception.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-15 22:29
 */
class DssNetworkChannelExceptionTest {

    @Test
    void constructor() {
        final DssNetworkChannelException exception = new DssNetworkChannelException();

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor1() {
        final DssNetworkChannelException exception = new DssNetworkChannelException("test");

        assertNotNull(exception);
        assertEquals("test", exception.getMessage());
    }

    @Test
    void constructor2() {
        final DssNetworkChannelException exception = new DssNetworkChannelException("test", new NullPointerException());

        assertNotNull(exception);
        assertEquals("test", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    @Test
    void constructor3() {
        final DssNetworkChannelException exception = new DssNetworkChannelException(new NullPointerException());

        assertNotNull(exception);
        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    @Test
    void constructor4() {
        final DssNetworkChannelException exception = new DssNetworkChannelException("test", new NullPointerException(), true, true);

        assertNotNull(exception);
        assertEquals("test", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    @Test()
    void testThrow() {
        assertThrows(DssNetworkChannelException.class, () -> {
            throw new DssNetworkChannelException();
        });
    }
}