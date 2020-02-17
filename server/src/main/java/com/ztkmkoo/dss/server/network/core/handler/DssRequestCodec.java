package com.ztkmkoo.dss.server.network.core.handler;

import com.ztkmkoo.dss.server.network.core.entity.DssRequest;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:02
 */
public interface DssRequestCodec<O, R extends DssRequest> {

    /**
     * Convert netty request object(origin) to DssRequest codec.
     * @param origin: netty request object. byte buffer or some decoded object.
     * @return : Specific DssRequest.
     */
    R convert(O origin);
}
