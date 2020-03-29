package com.ztkmkoo.dss.core.message.rest;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 15. 오전 1:27
 */
public class DssRestServiceActorCommandRequest extends DssRestMasterActorCommandRequest implements DssRestServiceActorCommand {

    public DssRestServiceActorCommandRequest(DssRestMasterActorCommandRequest request) {
        super(request.getChannelId(), request.getSender(), request.getMethodType(), request.getPath(), request.getContent());
    }
}
