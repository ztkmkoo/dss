package io.github.ztkmkoo.dss.core.service.rest;

import io.github.ztkmkoo.dss.core.service.DssServiceResponse;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:34
 */
public interface DssRestResponseConverter<S extends DssServiceResponse> extends DssRestEntityConverter<S, String> {
}
