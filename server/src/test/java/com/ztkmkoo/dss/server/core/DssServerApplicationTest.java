package com.ztkmkoo.dss.server.core;

import com.ztkmkoo.dss.server.core.exception.DssServerApplicationRuntimeException;
import com.ztkmkoo.dss.server.network.http.DssHttpServerProperty;
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
    public void run() throws InterruptedException {

        final DssServerApplicationProperty property = DssServerApplicationProperty
                .builder()
                .networkProperty(DssHttpServerProperty
                        .builder(false)
                        .build()
                )
                .build();
        assertNotNull(property);

        DssServerApplication.run(property);

        System.out.println("??? Here?");
    }
}