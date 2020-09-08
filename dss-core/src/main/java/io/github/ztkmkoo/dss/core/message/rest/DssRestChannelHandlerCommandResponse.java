package io.github.ztkmkoo.dss.core.message.rest;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:40
 */
@Getter
public class DssRestChannelHandlerCommandResponse implements DssRestChannelHandlerCommand {

    private static final long serialVersionUID = -8946124853931183362L;
    private final String channelId;
    private final Integer status;
    private final DssRestServiceResponse response;

    @Builder
    private DssRestChannelHandlerCommandResponse(String channelId, Integer status, DssRestServiceResponse response) {
        this.channelId = channelId;
        this.status = (Objects.nonNull(status) ? status : HttpResponseStatus.OK.code());
        this.response = response;
    }

    @Override
    public String toString() {
        return "DssRestChannelHandlerCommandResponse{" +
                "channelId: '" + channelId + "', " +
                "status: '" + status + "', " +
                "response: '" + response + "'" +
                "}";
    }
}
