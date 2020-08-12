package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingService;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 01:53
 */
public interface HttpClientService<S extends Serializable> extends DssBlockingService {

    DssBlockingRestCommand.DssHttpResponseCommand<S> getRequest(DssBlockingRestCommand.HttpGetRequest request);
}
