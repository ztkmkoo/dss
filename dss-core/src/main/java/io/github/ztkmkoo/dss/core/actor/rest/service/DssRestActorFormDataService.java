package io.github.ztkmkoo.dss.core.actor.rest.service;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 5. 오후 9:34
 */
@Slf4j
public abstract class DssRestActorFormDataService extends AbstractDssRestActorService<HashMap<String, Serializable>> {

    private static final DssRestContentInfo formDataContentInfo = DssRestContentInfo
            .builder()
            .contentType(DssRestContentType.APPLICATION_WWW_FORM_URL_ENCODED)
            .charset(CharsetUtil.UTF_8)
            .build();

    public DssRestActorFormDataService(String name, String path, DssRestMethodType methodType) {
        super(name, path, methodType, formDataContentInfo, DssRestContentInfo.APPLICATION_JSON_UTF8);
    }

    @Override
    protected HashMap<String, Serializable> getBody(DssRestServiceActorCommandRequest commandRequest) {
        return getBody(commandRequest.getContent());
    }

    @SuppressWarnings("unchecked")
    protected final HashMap<String, Serializable> getBody(String content) {
        if (StringUtils.isEmpty(content)) {
            return new HashMap<>();
        }

        final String[] splits = content.split("&");
        final HashMap<String, Serializable> bodyMap = new HashMap<>(splits.length);

        for (int i = 0; i < splits.length; i++) {
            final String element = splits[i];
            if (!StringUtils.isEmpty(element)) {
                final String[] formData = element.split("=");
                if (formData.length == 2) {
                    final String name = formData[0];
                    final String preValue = formData[1];
                    bodyMap.put(name, preValue);
                } else {
                    log.warn("Parse form data error: {}", element);
                }
            }
        }

        return bodyMap;
    }
}
