package io.github.ztkmkoo.dss.core.network.core.channel;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-18 16:50
 */
public interface DssChannelFactory<T extends DssChannel> {
    /**
     * Creates a new channel.
     */
    T newChannel();
}
