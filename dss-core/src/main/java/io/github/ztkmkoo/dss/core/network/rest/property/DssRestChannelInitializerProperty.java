package io.github.ztkmkoo.dss.core.network.rest.property;

import io.netty.handler.ssl.SslContext;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-17 06:51
 */
@Getter @Setter
public class DssRestChannelInitializerProperty implements Serializable {
    private static final long serialVersionUID = 5457087029018205838L;

    private final boolean ssl;
    private final transient SslContext sslCtx;

    public DssRestChannelInitializerProperty(boolean ssl, SslContext sslCtx) {
        this.ssl = ssl;
        this.sslCtx = sslCtx;
    }

    public DssRestChannelInitializerProperty(boolean ssl) {
        this(ssl, null);
    }

    public DssRestChannelInitializerProperty() {
        this(false, null);
    }
}
