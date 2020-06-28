package io.github.ztkmkoo.dss.core.network.rest.property;

import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import lombok.Builder;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:44
 */
public class DssRestChannelProperty extends DssChannelProperty {

    private static final long serialVersionUID = -6904414304979344025L;

    @Builder
    private DssRestChannelProperty(String host, int port) {
        super(host, port);
    }
}
