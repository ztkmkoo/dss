package com.ztkmkoo.dss.network.common;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public interface DssServer {

    void bind(DssServerProperty property) throws InterruptedException;
}
