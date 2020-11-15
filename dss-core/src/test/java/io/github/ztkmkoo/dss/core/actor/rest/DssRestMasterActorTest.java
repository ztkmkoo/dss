package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommand;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:57
 */
class DssRestMasterActorTest extends AbstractDssActorTest {

    private static final TestProbe<DssRestChannelHandlerCommand> probe = testKit.createTestProbe();
    private static final ActorRef<DssMasterCommand> restMasterActorRef = testKit.spawn(DssRestMasterActor.create(), "rest-master");

    private static List<DssRestActorService> testServiceList() {
        final List<DssRestActorService> serviceList = new ArrayList<>();
        serviceList.add(new DssRestActorJsonService("hi", "/test", DssRestMethodType.GET) {

            @Override
            protected DssRestServiceResponse handlingRequest(DssRestServiceRequest request) {
                return null;
            }
        });

        return serviceList;
    }
}