package com.ztkmkoo.dss.core.actor.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import com.ztkmkoo.dss.core.util.StringUtils;
import io.netty.util.CharsetUtil;
import lombok.Getter;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 29. 오전 6:37
 */
@Getter
public abstract class DssRestActorJsonService<S extends Serializable> extends AbstractDssRestActorService<S> {

    private static final TypeReference<HashMap<String, Serializable>> DEFAULT_TYPE_REFERENCE = new TypeReference<HashMap<String, Serializable>>() {};
    private final TypeReference<S> typeReference = new TypeReference<S>() {};

    public DssRestActorJsonService(
            String name,
            String path,
            DssRestMethodType methodType,
            Charset consumeCharset,
            DssRestContentInfo produce) {
        super(
                name,
                path,
                methodType,
                fromCharset(consumeCharset),
                (Objects.nonNull(produce) ? produce : DssRestContentInfo.APPLICATION_JSON_UTF8)
        );
    }

    public DssRestActorJsonService(
            String name,
            String path,
            DssRestMethodType methodType
    ) {
        this(name, path, methodType, CharsetUtil.UTF_8, DssRestContentInfo.APPLICATION_JSON_UTF8);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected S getBody(String content) {
        if (StringUtils.isEmpty(content)) {
            if (DEFAULT_TYPE_REFERENCE.getType().equals(typeReference.getType())) {
                return (S) new HashMap<String, Serializable>();
            }

            return null;
        }

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static DssRestContentInfo fromCharset(Charset charset) {
        if (Objects.isNull(charset)) {
            return DssRestContentInfo.APPLICATION_JSON_UTF8;
        }

        if (charset.equals(CharsetUtil.UTF_8)) {
            return DssRestContentInfo.APPLICATION_JSON_UTF8;
        } else {
            return DssRestContentInfo
                    .builder()
                    .contentType(DssRestContentType.APPLICATION_JSON)
                    .charset(charset)
                    .build();
        }
    }
}