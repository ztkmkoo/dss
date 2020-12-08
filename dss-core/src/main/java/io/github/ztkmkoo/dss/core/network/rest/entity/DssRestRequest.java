package io.github.ztkmkoo.dss.core.network.rest.entity;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 9:40
 */
public interface DssRestRequest extends Serializable {

    String getPath();

    DssRestMethodType getMethodType();

    DssRestContentType getRestContentType();
}
