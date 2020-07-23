package io.github.ztkmkoo.dss.core.network.core.entity;

import io.netty.util.internal.ObjectUtil;
import lombok.Getter;

import java.nio.channels.SelectionKey;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-24 01:51
 */
@Getter
public class DssSelectionKeyWrapper {

    private final SelectionKey selectionKey;
    private final long openMillis;

    public DssSelectionKeyWrapper(SelectionKey selectionKey) {
        this.selectionKey = ObjectUtil.checkNotNull(selectionKey, "selectionKey");
        this.openMillis = System.currentTimeMillis();
    }
}
