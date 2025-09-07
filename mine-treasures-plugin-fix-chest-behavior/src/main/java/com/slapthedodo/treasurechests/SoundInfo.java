package com.slapthedodo.treasurechests;

public class SoundInfo {

    private final String name;
    private final boolean broadcast;

    public SoundInfo(String name, boolean broadcast) {
        this.name = name;
        this.broadcast = broadcast;
    }

    public String getName() {
        return name;
    }

    public boolean shouldBroadcast() {
        return broadcast;
    }
}
