package com.uname1337.uclock;

import java.util.Objects;

public record ClockRegion(
        String id,
        String worldName,
        int minX,
        int minY,
        int minZ,
        int maxX,
        int maxY,
        int maxZ
) {
    public ClockRegion {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(worldName, "worldName");
    }
}
