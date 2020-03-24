package com.ztkmkoo.dss.core.message.rest;

import lombok.Builder;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 20. 오전 12:30
 */
@Getter
public class DssRestChannelHandlerCommandInvalidUriResponse implements DssRestChannelHandlerCommand {

    private static final long serialVersionUID = 1333717034591553428L;
    private final String channelId;

    @Builder
    private DssRestChannelHandlerCommandInvalidUriResponse(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "DssRestChannelHandlerCommandResponse{" +
                "channelId: '" + channelId + "'" +
                "}";
    }
}
