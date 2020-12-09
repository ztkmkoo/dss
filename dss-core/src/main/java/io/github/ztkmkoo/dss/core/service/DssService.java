package io.github.ztkmkoo.dss.core.service;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 01:22
 */
public interface DssService<R extends DssServiceRequest, S extends DssServiceResponse> {

    void start(R request);

    void end(S response);
}
