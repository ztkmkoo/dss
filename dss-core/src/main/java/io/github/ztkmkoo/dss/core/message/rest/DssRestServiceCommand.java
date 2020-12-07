package io.github.ztkmkoo.dss.core.message.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-05 23:44
 */
public interface DssRestServiceCommand extends DssServiceCommand {

    @Getter
    class RestRequest extends DssRestCommand.RestRequest implements DssRestServiceCommand, DssServiceRequestCommand {
        private static final long serialVersionUID = 336127423557058855L;

        private final String channelId;
        private final ActorRef<DssResolverCommand> sender;

        public RestRequest(Builder builder) {
            super(builder);
            this.channelId = builder.channelId;
            this.sender = builder.sender;
        }

        public static Builder builder(DssRestResolverCommand.RestRequest restRequest, String channelId, ActorRef<DssResolverCommand> sender) {
            return new Builder(restRequest, channelId, sender);
        }

        public static class Builder extends DssRestCommand.RestRequest.Builder<DssRestServiceCommand.RestRequest> {

            private final String channelId;
            private final ActorRef<DssResolverCommand> sender;

            public Builder(DssRestResolverCommand.RestRequest restRequest, String channelId, ActorRef<DssResolverCommand> sender) {
                this.channelId = channelId;
                this.sender = sender;

                super.methodType(restRequest.getMethodType());
                super.contentType(restRequest.getContentType());
                super.path(restRequest.getPath());
                super.content(restRequest.getContent());
            }

            @Override
            public DssRestServiceCommand.RestRequest build() {
                return new DssRestServiceCommand.RestRequest(this);
            }
        }
    }
}
