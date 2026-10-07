package com.uname1337.uclock;

import org.bukkit.Location;

public class Selection {
    private Location first;
    private Location second;

    public boolean hasFirst() {
        return first != null;
    }

    public boolean hasSecond() {
        return second != null;
    }

    public boolean isComplete() {
        return first != null && second != null;
    }

    public void setFirst(Location first) {
        this.first = first.clone();
    }

    public void setSecond(Location second) {
        this.second = second.clone();
    }

    public Location getFirst() {
        return first;
    }

    public Location getSecond() {
        return second;
    }

    public void clear() {
        first = null;
        second = null;
    }
}
