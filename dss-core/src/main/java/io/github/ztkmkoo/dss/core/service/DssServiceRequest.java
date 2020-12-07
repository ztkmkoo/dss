package io.github.ztkmkoo.dss.core.service;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 03:42
 */
public interface DssServiceRequest extends Serializable {

    long getSeq();

    void setSeq(long seq);
}
