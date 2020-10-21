package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.CommonStaticContents;
import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.github.ztkmkoo.dss.core.util.ObjectUtils;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:57
 */
@Getter
public class DssBindCommand implements DssCommand {
    private static final long serialVersionUID = 6286330685953848759L;

    private final String host;
    private final int port;
    private final DssLogLevel logLevel;
    private final boolean ssl;
    private final String privateKeyPath;
    private final String certificatePath;
    private final String password;

    public <T extends DssBindCommand> DssBindCommand(Builder<T> builder) {
        this.host = ObjectUtils.defaultValueIfNull(builder.host, CommonStaticContents.getDefaultHost(), CommonStaticContents.getTextHost());
        this.port = ObjectUtils.defaultValueIfNull(builder.port, CommonStaticContents.getDefaultPort(), CommonStaticContents.getTextPort());
        this.logLevel = ObjectUtils.defaultValueIfNull(builder.logLevel, CommonStaticContents.getDefaultLogLevel(), CommonStaticContents.getTextLogLevel());
        this.ssl = builder.ssl;
        this.privateKeyPath = builder.privateKeyPath;
        this.certificatePath = builder.certificatePath;
        this.password = builder.password;
    }

    public abstract static class Builder <T extends DssBindCommand> {
        private String host;
        private Integer port;
        private DssLogLevel logLevel;
        private boolean ssl;
        private String privateKeyPath;
        private String certificatePath;
        private String password;

        public abstract T build();

        public Builder<T> host(String host) {
            this.host = host;
            return this;
        }

        public Builder<T> port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder<T> logLevel(DssLogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder<T> ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public Builder<T> privateKeyPath(String privateKeyPath) {
            this.privateKeyPath = privateKeyPath;
            return this;
        }

        public Builder<T> certificatePath(String certificatePath) {
            this.certificatePath = certificatePath;
            return this;
        }

        public Builder<T> password(String password) {
            this.password = password;
            return this;
        }
    }
}
