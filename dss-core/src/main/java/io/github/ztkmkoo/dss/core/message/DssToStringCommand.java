package io.github.ztkmkoo.dss.core.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-23 01:01
 */
public class DssToStringCommand implements DssCommand {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
