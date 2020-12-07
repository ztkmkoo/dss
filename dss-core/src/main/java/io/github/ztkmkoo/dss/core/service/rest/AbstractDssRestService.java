package io.github.ztkmkoo.dss.core.service.rest;

import io.github.ztkmkoo.dss.core.exception.io.DssDeserializeRuntimeException;
import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceCommand;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.service.AbstractDssService;
import io.github.ztkmkoo.dss.core.service.DssServiceRequest;
import io.github.ztkmkoo.dss.core.service.DssServiceResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 03:53
 */
@Slf4j
@Getter
public abstract class AbstractDssRestService<R extends DssServiceRequest, S extends DssServiceResponse> extends AbstractDssService<DssRestServiceCommand.RestRequest, DssRestResolverCommand.RestResponse, R, S> implements DssRestService<R, S> {

    private final String name;
    private final String path;
    private final DssRestMethodType methodType;
    private final DssRestRequestConverter<R> consumeConverter;
    private final DssRestResponseConverter<S> produceConverter;

    public AbstractDssRestService(
            String name,
            String path,
            DssRestMethodType methodType,
            DssRestRequestConverter<R> consumeConverter,
            DssRestResponseConverter<S> produceConverter
    ) {
        this.name = name;
        this.path = path;
        this.methodType = Objects.requireNonNull(methodType);
        this.consumeConverter = Objects.requireNonNull(consumeConverter);
        this.produceConverter = Objects.requireNonNull(produceConverter);

        Objects.requireNonNull(consumeConverter.getContentType());
        Objects.requireNonNull(produceConverter.getContentType());
    }

    @Override
    public R makeRequest(DssRestServiceCommand.RestRequest request, long seq) {
        final R req = consumeConverter.convert(request.getContent());
        req.setSeq(seq);
        return req;
    }

    @Override
    public DssRestResolverCommand.RestResponse makeResponse(S response, String channelId) {
        final DssRestResolverCommand.RestResponse.RestResponseBuilder builder = DssRestResolverCommand.RestResponse.builder();
        builder.channelId(channelId);

        try {
            final String body = produceConverter.convert(response);
            builder
                    .status(HttpResponseStatus.OK.code())
                    .body(body);
        } catch (DssDeserializeRuntimeException e) {
            log.error("Make response error", e);
            builder.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        }

        return builder.build();
    }

    @Override
    public DssRestContentType getConsumeType() {
        return consumeConverter.getContentType();
    }

    @Override
    public DssRestContentType getProduceType() {
        return produceConverter.getContentType();
    }
}