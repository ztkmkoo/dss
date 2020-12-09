package io.github.ztkmkoo.dss.core.service.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ztkmkoo.dss.core.exception.io.DssDeserializeRuntimeException;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.service.DssServiceRequest;
import io.github.ztkmkoo.dss.core.util.StringUtils;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:36
 */
public class DssRestRequestJsonConverter<D extends DssServiceRequest> implements DssRestRequestConverter<D> {

    private final TypeReference<D> typeReference;

    public DssRestRequestJsonConverter(TypeReference<D> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public D convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(source, typeReference);
        } catch (Exception e) {
            throw new DssDeserializeRuntimeException(e);
        }
    }

    @Override
    public DssRestContentType getContentType() {
        return DssRestContentType.APPLICATION_JSON;
    }
}
