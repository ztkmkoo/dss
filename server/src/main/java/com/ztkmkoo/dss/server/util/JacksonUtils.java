package com.ztkmkoo.dss.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 27. 오전 2:04
 */
public class JacksonUtils {

    private JacksonUtils() {}

    public static  <T extends Serializable> T readJsonString(String json, TypeReference<T> typeReference) throws JsonProcessingException {

        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, typeReference);
    }
}
