package io.github.ztkmkoo.dss.core.exception.handler;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;

public interface ExceptionHandleMethod {
    DssRestServiceResponse handlingException(DssRestServiceActorCommandRequest request);
}
