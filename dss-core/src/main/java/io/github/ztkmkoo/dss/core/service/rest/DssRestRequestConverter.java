package io.github.ztkmkoo.dss.core.service.rest;

import io.github.ztkmkoo.dss.core.service.DssServiceRequest;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:34
 */
public interface DssRestRequestConverter<D extends DssServiceRequest> extends DssRestEntityConverter<String, D> {
}
