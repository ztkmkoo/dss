package io.github.ztkmkoo.dss.core.network.rest.property;

import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:44
 */
@Getter
public class DssRestChannelProperty implements DssChannelProperty {

    private static final long serialVersionUID = -6904414304979344025L;
    private final String host;
    private final int port;

    @Builder
    private DssRestChannelProperty(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
