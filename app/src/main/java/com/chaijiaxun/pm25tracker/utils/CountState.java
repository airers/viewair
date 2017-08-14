package com.chaijiaxun.pm25tracker.utils;

/**
 * List of states for the reading count
 */

public enum CountState {
    IDLE (0),
    GETTING_READINGS (1),
    GOT_READINGS (2);

    private final int type;

    CountState(int i) {
        type = i;
    }

    public int toInt() {
        return this.type;
    }
}
