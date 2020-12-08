package io.github.ztkmkoo.dss.core.network.rest;

import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.rest.entity.DssRestRequest;
import io.github.ztkmkoo.dss.core.util.StringUtils;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-22 07:22
 */
public interface DssRestChannelHandler {

    default DssRestResolverCommand.RestRequest makeRequestCommand(DssChannelHandlerContext ctx, DssRestRequest restRequest, String content) {
        Objects.requireNonNull(restRequest);

        return DssRestResolverCommand.RestRequest
                .builder()
                .ctx(Objects.requireNonNull(ctx))
                .path(StringUtils.requireNonEmpty(restRequest.getPath()))
                .methodType(Objects.requireNonNull(restRequest.getMethodType()))
                .contentType(Objects.requireNonNull(restRequest.getRestContentType()))
                .content(content)
                .build();
    }
}
