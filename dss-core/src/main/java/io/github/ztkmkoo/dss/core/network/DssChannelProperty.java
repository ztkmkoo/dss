package io.github.ztkmkoo.dss.core.network;

import io.github.ztkmkoo.dss.core.network.exception.NotValidDssPropertyParameter;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public abstract class DssChannelProperty implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DssChannelProperty.class);

    private static final String LOCAL_HOST_STRING = "localhost";
    private static final Pattern HOST_IPV4_PATTERN = Pattern.compile("\\w+\\.\\w+\\.\\w+\\.\\w+");
    private static final Pattern HOST_DOMAIN_NAME_PATTERN = Pattern.compile("^([a-z0-9])(([a-z0-9-]{1,61})?[a-z0-9]{1})?(\\.[a-z0-9](([a-z0-9-]{1,61})?[a-z0-9]{1})?)?(\\.[a-zA-Z]{2,4})+$");

    private final String host;
    private final int port;

    protected DssChannelProperty(String host, int port) {
        this.host = host;
        this.port = port;

        validateProperty();
    }

    private void validateProperty() {
        validateHost(host);
    }

    public static void validateHost(String host) {
        if (StringUtils.isEmpty(host)) {
            throw new NotValidDssPropertyParameter("Host is empty");
        }

        if (LOCAL_HOST_STRING.equals(host)) {
            return;
        }

        if (isHostIpv4(host)) {
            return;
        }
        log.debug("{} is not a ipv4 type", host);

        if (isDomain(host)) {
            return;
        }
        log.debug("{} is not a domain type", host);

        throw new NotValidDssPropertyParameter("Invalid host name for domain or ipv4");
    }

    public static void validatePort(int port) {
        if (port >= 1 && port <= 65535) {
            return;
        }

        throw new NotValidDssPropertyParameter("Invalid port number");
    }

    private static boolean isHostIpv4(String host) {
        return matchesPattern(host, HOST_IPV4_PATTERN);
    }

    private static boolean isDomain(String host) {
        return matchesPattern(host, HOST_DOMAIN_NAME_PATTERN);
    }

    private static boolean matchesPattern(String source, Pattern p) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(p);

        final Matcher m = p.matcher(source);
        return m.matches();
    }
}
