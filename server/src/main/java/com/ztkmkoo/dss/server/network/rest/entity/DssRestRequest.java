package com.ztkmkoo.dss.server.network.rest.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztkmkoo.dss.server.network.core.entity.DssRequest;
import com.ztkmkoo.dss.server.network.rest.enumeration.DssRestMethod;
import lombok.Getter;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오후 10:28
 */
public class DssRestRequest implements DssRequest {

    private static final long serialVersionUID = 777263538444345409L;

    @Getter
    private final String uri;
    @Getter
    private final DssRestMethod method;
    @Getter
    private final String content;

    private DssRestRequest(Builder builder) {
        this.uri = builder.uri;
        this.method = builder.method;
        this.content = builder.content;
    }

    @Override
    public String toString() {
        return "DssRestRequest {\n" +
                "uri=" + uri + ",\n" +
                "method=" + method + ",\n" +
                "content=" + content + "\n" +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public <T extends Serializable> T getContentByTypedReference(TypeReference<T> typeReference) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return getContentByTypedReference(mapper, typeReference);
    }

    public <T extends Serializable> T getContentByTypedReference(ObjectMapper mapper, TypeReference<T> typeReference) throws JsonProcessingException {
        return mapper.readValue(content, typeReference);
    }

    public static class Builder {

        private String uri;
        private DssRestMethod method;
        private String content;

        private Builder() {}

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder method(DssRestMethod method) {
            this.method = method;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public DssRestRequest build() {
            return new DssRestRequest(this);
        }
    }
}
