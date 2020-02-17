package com.ztkmkoo.dss.server.core.entity;

import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 2:18
 */
public class ServerTypeEntityTest {

    @Test
    public void of() {
        final ServerTypeEntity entity = ServerTypeEntity.of(DssNetworkType.HTTP, 9090);
        assertNotNull(entity);
        assertEquals(DssNetworkType.HTTP, entity.getNetworkType());
        assertEquals(9090, entity.getPort());
    }

    @Test
    public void getNetworkType() {
        final ServerTypeEntity entity = ServerTypeEntity.of(DssNetworkType.HTTP, 9090);
        assertEquals(DssNetworkType.HTTP, entity.getNetworkType());
    }

    @Test
    public void getPort() {
        final ServerTypeEntity entity = ServerTypeEntity.of(DssNetworkType.HTTP, 9090);
        assertEquals(9090, entity.getPort());
    }
}