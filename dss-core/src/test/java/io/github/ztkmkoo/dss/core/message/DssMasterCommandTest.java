package io.github.ztkmkoo.dss.core.message;

import akka.actor.Address;
import akka.actor.RootActorPath;
import akka.actor.testkit.typed.internal.DebugRef;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:20
 */
class DssMasterCommandTest {

    @Test
    void bind() {
        final DssMasterCommand.Bind command = DssMasterCommand.Bind
                .builder()
                .host("127.0.0.1")
                .port(1234)
                .logLevel(DssLogLevel.INFO)
                .build();

        DssCommandHelpUtils.assertBindCommand(command, "127.0.0.1", 1234, DssLogLevel.INFO);
    }

    @Test
    void bindDefault() {
        final DssMasterCommand.Bind defaultCommand = DssMasterCommand.Bind.builder().build();
        DssCommandHelpUtils.assertDefaultBindCommand(defaultCommand);
    }

    @Test
    void statusRequest() {
        final ActorRef<DssCommand> actorRef = new DebugRef<>(new RootActorPath(new Address("akka", "test"), "test"), true);
        final DssMasterCommand.StatusRequest request = DssMasterCommand.StatusRequest.builder().sender(actorRef).build();
        assertNotNull(request);
        assertNotNull(request.getSender());

        assertEquals("test", request.getSender().path().name());
    }

    @Test
    void statusResponse() {
        final DssMasterCommand.StatusResponse response = DssMasterCommand.StatusResponse.builder().status(DssMasterActorStatus.PENDING).build();
        assertNotNull(response);
        assertEquals(DssMasterActorStatus.PENDING, response.getStatus());
    }

    @Test
    void statusUpdate() {
        final DssMasterCommand.StatusUpdate update = DssMasterCommand.StatusUpdate.builder().status(DssMasterActorStatus.START).build();
        assertNotNull(update);
        assertEquals(DssMasterActorStatus.START, update.getStatus());
    }
}