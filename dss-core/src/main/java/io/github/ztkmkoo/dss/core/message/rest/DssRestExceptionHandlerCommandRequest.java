package io.github.ztkmkoo.dss.core.message.rest;

import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DssRestExceptionHandlerCommandRequest implements DssRestExceptionHandlerCommand {
    private static final long serialVersionUID = 5752260184738368572L;

    private final DssRestActorService service;
    private final DssRestServiceActorCommandRequest request;
    private final Exception exception;

    @Builder
    public DssRestExceptionHandlerCommandRequest(DssRestActorService service, DssRestServiceActorCommandRequest request, Exception exception) {
        this.service = service;
        this.request = request;
        this.exception = exception;
    }
}
