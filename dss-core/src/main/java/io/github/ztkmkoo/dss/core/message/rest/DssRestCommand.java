package io.github.ztkmkoo.dss.core.message.rest;

import io.github.ztkmkoo.dss.core.message.DssCommand;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 14:21
 */
public interface DssRestCommand extends DssCommand {

    @Getter
    class RestRequest implements DssRestCommand {
        private static final long serialVersionUID = 3011192701264316440L;

        private final DssRestMethodType methodType;
        private final DssRestContentType contentType;
        private final String path;
        private final String content;

        public <T extends RestRequest> RestRequest(Builder<T> builder) {
            this.methodType = Objects.requireNonNull(builder.methodType);
            this.contentType = Objects.requireNonNull(builder.contentType);
            this.path = StringUtils.requireNonEmpty(builder.path);
            this.content = builder.content;
        }

        public abstract static class Builder <T extends RestRequest> {

            private DssRestMethodType methodType;
            private DssRestContentType contentType;
            private String path;
            private String content;

            public abstract T build();

            public Builder<T> methodType(DssRestMethodType methodType) {
                this.methodType = methodType;
                return this;
            }

            public Builder<T> contentType(DssRestContentType contentType) {
                this.contentType = contentType;
                return this;
            }

            public Builder<T> path(String path) {
                this.path = path;
                return this;
            }

            public Builder<T> content(String content) {
                this.content = content;
                return this;
            }
        }

        @Override
        public String toString() {
            return "path: " + path + ", methodType: " + methodType + ", contentType: " + contentType + ", content: " + content;
        }
    }
}
