package io.github.ztkmkoo.dss.core.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 03:43
 */
@JsonIgnoreProperties(value = { "seq" })
public interface DssServiceResponse extends Serializable {

    long getSeq();
}
