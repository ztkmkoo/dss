package com.ztkmkoo.dss.core.message.rest;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import lombok.Builder;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:40
 */
@Getter
public class DssRestChannelHandlerCommandResponse implements DssRestChannelHandlerCommand {

    private static final long serialVersionUID = -8946124853931183362L;
    private final String channelId;
    private final DssRestServiceResponse response;

    @Builder
    private DssRestChannelHandlerCommandResponse(String channelId, DssRestServiceResponse response) {
        this.channelId = channelId;
        this.response = response;
    }

    @Override
    public String toString() {
        return "DssRestChannelHandlerCommandResponse{" +
                "channelId: '" + channelId + "', " +
                "response: '" + response + "'" +
                "}";
    }
}
