package io.github.ztkmkoo.dss.core.network;

import io.github.ztkmkoo.dss.core.network.exception.NotValidDssPropertyParameter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DssChannelPropertyTest {

    @Test(expected = NotValidDssPropertyParameter.class)
    public void validateHostWithEmptyHost() {
        DssChannelProperty.validateHost("");
    }

    @Test(expected = NotValidDssPropertyParameter.class)
    public void validateHostWithErrorHost() {
        DssChannelProperty.validateHost("127.0.0");
    }

    @Test
    public void validateHostWithDomain() {
        validateHost("www.google.com");
    }

    @Test
    public void validateHostWithIpv4() {
        validateHost("127.0.0.1");
    }

    @Test
    public void validateHostWithLocalHost() {
        validateHost("localhost");
    }

    private void validateHost(String host) {
        DssChannelProperty.validateHost(host);
        assertTrue(true);
    }

    @Test(expected = NotValidDssPropertyParameter.class)
    public void validatePortWithErrorPort() {
        DssChannelProperty.validatePort(0);
    }

    @Test
    public void validatePort() {
        DssChannelProperty.validatePort(8080);
        assertTrue(true);
    }
}