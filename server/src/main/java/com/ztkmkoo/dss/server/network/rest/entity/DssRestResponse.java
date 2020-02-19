package com.ztkmkoo.dss.server.network.rest.entity;

import com.ztkmkoo.dss.server.network.core.entity.DssResponse;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 20. 오전 12:05
 */
public interface DssRestResponse extends DssResponse {

    int getStatus();

    String getMessage();

    boolean isSuccessful();
}
