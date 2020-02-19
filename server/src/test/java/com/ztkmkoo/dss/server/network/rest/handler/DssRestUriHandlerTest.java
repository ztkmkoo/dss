package com.ztkmkoo.dss.server.network.rest.handler;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 20. 오전 1:19
 */
public class DssRestUriHandlerTest {

    @Test
    public void getUri() {

        final DssRestUriHandler handler = () -> "/dogs/happy/age";
        assertEquals("/dogs/happy/age", handler.getUri());
    }

    @Test
    public void sameUri() {
        final DssRestUriHandler handler = () -> "/dogs/happy/age";

        assertTrue(handler.sameUri("/dogs/happy/age"));
        assertTrue(handler.sameUri("/dogs/happy/age/"));
        assertTrue(handler.sameUri("dogs/happy/age/"));
        assertTrue(handler.sameUri("dogs/happy/age"));

        assertFalse(handler.sameUri("dogs/happy"));
        assertFalse(handler.sameUri("dogs/happi/age"));

        assertTrue(handler.sameUri("dogs/happy/age?"));
        assertTrue(handler.sameUri("dogs/happy/age?nationality=kr&&birthday=20200128"));
    }

    @Test
    public void sameUriWithPathVariable() {
        final DssRestUriHandler handler = () -> "/dogs/{name}/age";

        assertTrue(handler.sameUri("/dogs/happy/age"));
        assertTrue(handler.sameUri("/dogs/happy/age/"));
        assertTrue(handler.sameUri("dogs/happy/age/"));
        assertTrue(handler.sameUri("dogs/happy/age"));

        assertTrue(handler.sameUri("dogs/happy/age?"));
        assertTrue(handler.sameUri("dogs/happy/age?nationality=kr&&birthday=20200128"));
    }

    @Test
    public void getFixedUri() {

        final String uri = "/dogs/happy/age";
        final String fixed = DssRestUriHandler.getFixedUri(uri);
        assertEquals("dogs/happy/age", fixed);
    }

    @Test
    public void getFixedUriWithQueryString() {

        final String uri = "/dogs/happy/age?nationality=kr";
        final String fixed = DssRestUriHandler.getFixedUri(uri);
        assertEquals("dogs/happy/age", fixed);
    }

    @Test
    public void isPathVariable() {
        assertTrue(DssRestUriHandler.isPathVariable("{name}"));
        assertFalse(DssRestUriHandler.isPathVariable("name"));
    }
}