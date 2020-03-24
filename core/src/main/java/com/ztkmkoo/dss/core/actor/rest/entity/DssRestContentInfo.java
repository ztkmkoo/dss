package com.ztkmkoo.dss.core.actor.rest.entity;

import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.netty.util.CharsetUtil;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 25. 오전 1:50
 */
@Getter
public class DssRestContentInfo {

    public static final DssRestContentInfo APPLICATION_JSON_UTF8 = DssRestContentInfo
            .builder()
            .contentType(DssRestContentType.APPLICATION_JSON)
            .charset(CharsetUtil.UTF_8)
            .build();
    private static final DssRestContentType DEFAULT_CONTENT_TYPE = DssRestContentType.APPLICATION_JSON;
    private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

    private final DssRestContentType contentType;
    private final Charset charset;

    @Builder
    private DssRestContentInfo(DssRestContentType contentType, Charset charset) {
        this.contentType = (Objects.nonNull(contentType) ? contentType : DEFAULT_CONTENT_TYPE);
        this.charset = (Objects.nonNull(charset) ? charset : DEFAULT_CHARSET);
    }
}
