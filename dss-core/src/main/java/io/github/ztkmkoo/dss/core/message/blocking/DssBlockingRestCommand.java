package io.github.ztkmkoo.dss.core.message.blocking;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssRequestCommand;
import io.github.ztkmkoo.dss.core.message.DssResponseCommand;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-12 23:56
 */
public interface DssBlockingRestCommand extends DssBlockingCommand {

    @Getter
    abstract class HttpCommand implements DssBlockingRestCommand {
        private static final long serialVersionUID = 1115032460790263234L;

        private final long seq;
        private final ActorRef<DssBlockingRestCommand> sender;

        private HttpCommand(Builder builder) {
            this.seq = builder.seq;
            this.sender = builder.sender;
        }

        protected static <K, V> Map<K, V> unmodifiableMap(Map<K, V> origin) {
            Objects.requireNonNull(origin);
            return Collections.unmodifiableMap(origin.isEmpty() ? Collections.emptyMap() : origin);
        }

        protected static class Builder {
            private final long seq;
            private final ActorRef<DssBlockingRestCommand> sender;

            protected Builder(long seq, ActorRef<DssBlockingRestCommand> sender) {
                this.seq = seq;
                this.sender = sender;
            }
        }
    }

    interface DssHttpRequestCommand extends DssRequestCommand {

        DssRestMethodType getMethodType();

        String getUrl();

        Map<String, String> getHeader();
    }

    @Getter
    class HttpRequest extends HttpCommand implements DssHttpRequestCommand {
        private static final long serialVersionUID = 4693699468737347923L;

        private final DssRestMethodType methodType;
        private final String url;
        private final Map<String, String> header;

        protected HttpRequest(RequestBuilder builder) {
            super(builder);
            this.methodType = builder.methodType;
            this.url = builder.url;
            this.header = unmodifiableMap(builder.header);
        }

        protected static class RequestBuilder extends HttpCommand.Builder {
            private final DssRestMethodType methodType;
            private final String url;
            private final Map<String, String> header = new HashMap<>();

            private RequestBuilder(long seq, ActorRef<DssBlockingRestCommand> sender, DssRestMethodType methodType, String url) {
                super(seq, sender);
                this.methodType = methodType;
                this.url = url;
            }

            public Builder addHeader(String key, String value) {
                header.put(key, value);
                return this;
            }
        }
    }

    @Getter
    class HttpGetRequest extends HttpRequest {
        private static final long serialVersionUID = -514161223431032617L;

        private final Map<String, String> param;

        private HttpGetRequest(GetBuilder builder) {
            super(builder);
            this.param = unmodifiableMap(builder.param);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("HttpGetRequest: {\n")
                    .append("methodType=")
                    .append(getMethodType())
                    .append(",\n")
                    .append("url=")
                    .append(getUrl())
                    .append(",\n")
                    .append("header=")
                    .append(getHeader())
                    .append(",\n")
                    .append("param=")
                    .append(getParam())
                    .append("\n}")
                    .toString();
        }

        public static GetBuilder builder(long seq, ActorRef<DssBlockingRestCommand> sender, String url) {
            return new GetBuilder(seq, sender, url);
        }

        public static class GetBuilder extends RequestBuilder {

            private final Map<String, String> param = new HashMap<>();

            private GetBuilder(long seq, ActorRef<DssBlockingRestCommand> sender, String url) {
                super(seq, sender, DssRestMethodType.GET, url);
            }

            public GetBuilder addParam(String key, String value) {
                this.param.put(key, value);
                return this;
            }

            public HttpGetRequest build() {
                return new HttpGetRequest(this);
            }
        }
    }

    interface DssHttpResponseCommand<T extends Serializable> extends DssResponseCommand {

        int getCode();

        String getMessage();

        T getBody();
    }

    @Getter
    class HttpResponse<T extends Serializable> extends HttpCommand implements DssHttpResponseCommand<T> {
        private static final long serialVersionUID = 7967541866731813991L;

        private final int code;
        private final String message;
        private final T body;

        private HttpResponse(ResponseBuilder<T> builder) {
            super(builder);
            this.code = builder.code;
            this.message = builder.message;
            this.body = builder.body;
        }

        public static <T extends Serializable> ResponseBuilder<T> builder(long seq, ActorRef<DssBlockingRestCommand> sender, int code) {
            return new ResponseBuilder<>(seq, sender, code);
        }

        public static class ResponseBuilder<T extends Serializable> extends Builder {

            private final int code;
            private String message;
            private T body;

            protected ResponseBuilder(long seq, ActorRef<DssBlockingRestCommand> sender, int code) {
                super(seq, sender);
                this.code = code;
            }

            public ResponseBuilder<T> message(String message) {
                this.message = message;
                return this;
            }

            public ResponseBuilder<T> body(T body) {
                this.body = body;
                return this;
            }

            public HttpResponse<T> build() {
                return new HttpResponse<>(this);
            }
        }
    }
}
