package com.ztkmkoo.dss.core.message.rest;

import lombok.Builder;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:40
 */
@Getter
public class DssRestChannelHandlerCommandResponse implements DssRestChannelHandlerCommand {

    private final String channelId;

    @Builder
    private DssRestChannelHandlerCommandResponse(String channelId) {
        this.channelId = channelId;
    }
}
