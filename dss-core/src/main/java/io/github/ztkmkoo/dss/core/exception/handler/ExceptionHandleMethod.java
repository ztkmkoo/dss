package io.github.ztkmkoo.dss.core.exception.handler;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;

public interface ExceptionHandleMethod {
    DssRestServiceResponse handlingException(DssRestServiceRequest request);
}
