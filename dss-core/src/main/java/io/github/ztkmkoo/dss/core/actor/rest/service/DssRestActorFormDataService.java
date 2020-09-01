package io.github.ztkmkoo.dss.core.actor.rest.service;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 5. 오후 9:34
 */
@Slf4j
public abstract class DssRestActorFormDataService extends AbstractDssRestActorService<HashMap<String, Object>> {

    private static final DssRestContentInfo formDataContentInfo = DssRestContentInfo
            .builder()
            .contentType(DssRestContentType.MULTIPART_FORM_DATA)
            .charset(CharsetUtil.UTF_8)
            .build();

    public DssRestActorFormDataService(String name, String path, DssRestMethodType methodType) {
        super(name, path, methodType, formDataContentInfo, DssRestContentInfo.APPLICATION_JSON_UTF8);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final HashMap<String, Object> getBody(DssRestServiceActorCommandRequest commandRequest) {
        if (StringUtils.isEmpty(commandRequest.getContent())) {
            return new HashMap<>();
        }

        String[] splits = null;
        HashMap<String, Object> bodyMap = null;

        if (commandRequest.getBoundary().isEmpty()) {
            splits = commandRequest.getContent().split("&");
            bodyMap = new HashMap<>(splits.length);

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
        } else {
            splits = commandRequest.getContent().split(commandRequest.getBoundary() + "\r\n");
            bodyMap = new HashMap<>(splits.length);

            for (int i = 1; i < splits.length; ++i) {
                final String[] token = splits[i].split("\r\n");
                final String name = token[0].substring(token[0].indexOf("\"") + 1, token[0].lastIndexOf("\""));
                final String preValue = token[2];
                bodyMap.put(name, preValue);
            }
        }

        return bodyMap;
    }
}
