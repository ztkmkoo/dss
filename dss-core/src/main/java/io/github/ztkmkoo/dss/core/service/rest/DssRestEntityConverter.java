package io.github.ztkmkoo.dss.core.service.rest;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-08 02:32
 */
public interface DssRestEntityConverter<S extends Serializable, D extends Serializable> {

    D convert(S source);

    DssRestContentType getContentType();
}
