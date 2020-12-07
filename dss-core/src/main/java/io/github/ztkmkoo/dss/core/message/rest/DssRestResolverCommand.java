package io.github.ztkmkoo.dss.core.message.rest;

import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelHandlerContext;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 15:09
 */
public interface DssRestResolverCommand extends DssResolverCommand {

    @Getter
    class RestRequest extends DssRestCommand.RestRequest implements DssRestResolverCommand {
        private static final long serialVersionUID = 3546371930352499619L;

        private final transient DssChannelHandlerContext ctx;

        public RestRequest(Builder builder) {
            super(builder);
            this.ctx = Objects.requireNonNull(builder.ctx);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends DssRestCommand.RestRequest.Builder<DssRestResolverCommand.RestRequest> {

            private DssChannelHandlerContext ctx;

            public DssRestResolverCommand.RestRequest.Builder ctx(DssChannelHandlerContext ctx) {
                this.ctx = ctx;
                return this;
            }

            public DssRestResolverCommand.RestRequest build() {
                return new DssRestResolverCommand.RestRequest(this);
            }
        }

        @Override
        public String toString() {
            return super.toString() + ", ctx: " + ctx.getChannelId();
        }
    }

    @Getter
    class RestResponse implements DssRestResolverCommand, DssServiceResponseCommand {
        private static final long serialVersionUID = -5830533960652834426L;

        private final String channelId;
        private final int status;
        private final String body;

        @Builder
        public RestResponse(String channelId, int status, String body) {
            this.channelId = channelId;
            this.status = status;
            this.body = body;
        }

        @Override
        public String toString() {
            return "channelId: " + channelId + ", status: " + status + ", body: " + body;
        }
    }
}
