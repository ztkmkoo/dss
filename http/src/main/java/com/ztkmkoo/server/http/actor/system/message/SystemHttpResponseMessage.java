package com.ztkmkoo.server.http.actor.system.message;

import akka.http.javadsl.model.HttpResponse;
import com.ztkmkoo.server.http.actor.message.BaseMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SystemHttpResponseMessage extends BaseMessage {

    private final long seq;
    private final HttpResponse httpResponse;

    private SystemHttpResponseMessage(final Builder builder) {
        this.seq = builder.seq;
        this.httpResponse = builder.httpResponse;
    }

    public static class Builder {

        private Long seq;
        private HttpResponse httpResponse;

        private Builder() {}

        public Builder seq(final long seq) {

            this.seq = seq;
            return this;
        }

        public Builder httpResponse(final HttpResponse httpResponse) {

            this.httpResponse = httpResponse;
            return this;
        }

        public SystemHttpResponseMessage build() {

            Objects.requireNonNull(seq);
            Objects.requireNonNull(httpResponse);
            return new SystemHttpResponseMessage(this);
        }
    }
}
