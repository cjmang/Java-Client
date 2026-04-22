package io.github.archipelagomw.network;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.atomic.AtomicInteger;

public class APPacket {
    protected static final AtomicInteger requestIdGen = new AtomicInteger(1);

    @SerializedName("cmd")
    private APPacketType cmd;

    public APPacket(APPacketType cmd) {
        this.cmd = cmd;
    }

    public APPacketType getCmd() {
        return cmd;
    }
}
