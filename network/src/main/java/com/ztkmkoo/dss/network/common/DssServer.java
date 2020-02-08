package com.ztkmkoo.dss.network.common;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * Network server module interface.
 */
public interface DssServer {

    /**
     * Server binding method.
     * @param property: DssServerProperty pass the specific property
     * @throws InterruptedException: from configServerBootstrap or channel future sync
     */
    void bind(DssServerProperty property) throws InterruptedException;
}
