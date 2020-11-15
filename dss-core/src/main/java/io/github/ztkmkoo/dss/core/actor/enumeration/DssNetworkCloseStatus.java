package io.github.ztkmkoo.dss.core.actor.enumeration;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-11 23:50
 *
 *                                      +---------------------------+
 *                                      | Completed successfully    |
 *                                      +---------------------------+
 *                                 +---->      isDone() = true      |
 * +--------------------------+    |    |   isSuccess() = true      |
 * |        Uncompleted       |    |    +===========================+
 * +--------------------------+    |    | Completed with failure    |
 * |      isDone() = false    |    |    +---------------------------+
 * |   isSuccess() = false    |----+---->      isDone() = true      |
 * | isCancelled() = false    |    |    |       cause() = non-null  |
 * |       cause() = null     |    |    +===========================+
 * +--------------------------+    |    | Completed by cancellation |
 *                                 |    +---------------------------+
 *                                 +---->      isDone() = true      |
 *                                      | isCancelled() = true      |
 *                                      +---------------------------+
 */
public enum DssNetworkCloseStatus {
    UNCOMPLETED,
    SUCCESSFULLY,
    FAILED,
    CANCELLED,
    UNKNOWN,
    ;

    public static DssNetworkCloseStatus from(boolean isDone, boolean isSuccess, boolean isCancelled, Throwable cause) {
        if (isDone) {
            if (isSuccess) {
                return SUCCESSFULLY;
            } else if (Objects.nonNull(cause)) {
                return FAILED;
            } else if (isCancelled) {
                return CANCELLED;
            } else {
                return UNKNOWN;
            }
        } else {
            if (!isSuccess && !isCancelled && Objects.isNull(cause)) {
                return UNCOMPLETED;
            } else {
                return UNKNOWN;
            }
        }
    }
}
