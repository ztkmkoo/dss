package com.ztkmkoo.dss.server.core;

import com.ztkmkoo.dss.server.actor.rest.DssRestMasterActor;
import com.ztkmkoo.dss.server.actor.rest.DssRestServerActorProperty;
import com.ztkmkoo.dss.server.core.exception.DssServerApplicationRuntimeException;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.network.rest.DssRestChannelProperty;
import com.ztkmkoo.dss.server.network.rest.handler.DssRestChannelInitializer;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 11. 오전 12:42
 */
public class DssServerApplicationTest {

    @Test(expected = DssServerApplicationRuntimeException.class)
    public void runWithEmptyPropertyList() {
        DssServerApplication.run(Collections.emptyList());
    }


    @Test @Ignore("Local Test")
    public void run() {

        final DssServerApplicationProperty property = DssServerApplicationProperty
                .builder(DssNetworkType.REST)
                .networkProperty(
                        DssRestChannelProperty
                                .builder(DssRestChannelInitializer::new)
                                .build()
                )
                .dssServerActorProperty(
                        DssRestServerActorProperty
                                .builder(DssRestMasterActor.create())
                                .build())
                .build();
        assertNotNull(property);

        DssServerApplication.run(property);

        System.out.println("??? Here?");
    }
}