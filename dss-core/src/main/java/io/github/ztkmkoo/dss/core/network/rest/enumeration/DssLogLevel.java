package io.github.ztkmkoo.dss.core.network.rest.enumeration;

public enum DssLogLevel {

    ERROR(200), WARN(300), INFO(400), DEBUG(500), TRACE(600);

    private Integer  value;

    DssLogLevel(Integer value) {
        this.value = value;
    }

    public static DssLogLevel levelOf(Integer value) {
        for (DssLogLevel level : DssLogLevel.values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        return null;
    }
}
