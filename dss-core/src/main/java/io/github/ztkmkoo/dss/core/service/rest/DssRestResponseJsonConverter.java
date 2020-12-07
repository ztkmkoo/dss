package io.github.ztkmkoo.dss.core.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztkmkoo.dss.core.exception.io.DssDeserializeRuntimeException;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.service.DssServiceResponse;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:59
 */
public class DssRestResponseJsonConverter<S extends DssServiceResponse> implements DssRestResponseConverter<S> {

    @Override
    public String convert(S source) {
        if (Objects.isNull(source)) {
            return null;
        }

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(source);
        } catch (Exception e) {
            throw new DssDeserializeRuntimeException(e);
        }
    }

    @Override
    public DssRestContentType getContentType() {
        return DssRestContentType.APPLICATION_JSON;
    }
}
