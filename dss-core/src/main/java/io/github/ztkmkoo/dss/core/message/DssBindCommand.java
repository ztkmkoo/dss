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

    public <T extends DssBindCommand> DssBindCommand(Builder<T> builder) {
        this.host = ObjectUtils.defaultValueIfNull(builder.host, CommonStaticContents.getDefaultHost(), CommonStaticContents.getTextHost());
        this.port = ObjectUtils.defaultValueIfNull(builder.port, CommonStaticContents.getDefaultPort(), CommonStaticContents.getTextPort());
        this.logLevel = ObjectUtils.defaultValueIfNull(builder.logLevel, CommonStaticContents.getDefaultLogLevel(), CommonStaticContents.getTextLogLevel());
    }

    public abstract static class Builder <T extends DssBindCommand> {
        private String host;
        private Integer port;
        private DssLogLevel logLevel;

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
    }
}
