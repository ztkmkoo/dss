package io.github.ztkmkoo.dss.core.message.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssRequestCommand;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.ObjectUtils;
import io.netty.util.CharsetUtil;
import lombok.Getter;

import java.nio.charset.Charset;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-19 00:49
 */
@Getter
public class DssRestRequestCommand implements DssRequestCommand, DssRestMasterActorCommand, DssRestServiceActorCommand {
    private static final long serialVersionUID = -7239561288011565429L;

    private final String channelId;
    private final ActorRef<DssRestChannelHandlerCommand> sender;
    private final DssRestMethodType methodType;
    private final DssRestContentType contentType;
    private final String charset;
    private final String path;
    private final byte[] content;

    private DssRestRequestCommand(Builder builder) {
        this.channelId = builder.channelId;
        this.sender = builder.sender;
        this.methodType = builder.methodType;
        this.contentType = builder.contentType;
        this.charset = builder.charset;
        this.path = builder.path;

        final int length = builder.content.length;
        this.content = new byte[length];
        if (length > 0) {
            System.arraycopy(builder.content, 0, this.content, 0, builder.content.length);
        }
    }

    public static Builder builder(String channelId, ActorRef<DssRestChannelHandlerCommand> sender) {
        return new Builder(channelId, sender);
    }

    public static class Builder {

        private static final DssRestMethodType DEFAULT_METHOD_TYPE = DssRestMethodType.GET;
        private static final DssRestContentType DEFAULT_CONTENT_TYPE = DssRestContentType.APPLICATION_JSON;
        private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
        private static final String DEFAULT_PATH = "";
        private static final byte[] EMPTY_CONTENT = new byte[0];

        private final String channelId;
        private final ActorRef<DssRestChannelHandlerCommand> sender;

        private DssRestMethodType methodType;
        private DssRestContentType contentType;
        private String charset;
        private String path;
        private byte[] content;

        private Builder(String channelId, ActorRef<DssRestChannelHandlerCommand> sender) {
            this.channelId = channelId;
            this.sender = sender;
        }

        public Builder methodType(DssRestMethodType methodType) {
            this.methodType = methodType;
            return this;
        }

        public Builder contentType(DssRestContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder content(byte[] content) {
            this.content = content;
            return this;
        }

        public DssRestRequestCommand build() {
            this.methodType = ObjectUtils.getOrDefaultValue(this.methodType, DEFAULT_METHOD_TYPE);
            this.contentType = ObjectUtils.getOrDefaultValue(this.contentType, DEFAULT_CONTENT_TYPE);
            this.charset = ObjectUtils.getNonEmptyOrDefaultValue(this.charset, DEFAULT_CHARSET.name());
            this.path = ObjectUtils.getOrDefaultValue(this.path, DEFAULT_PATH);
            this.content = ObjectUtils.getOrDefaultValue(this.content, EMPTY_CONTENT);

            return new DssRestRequestCommand(this);
        }
    }
}
