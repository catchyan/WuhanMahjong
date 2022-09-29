package com.catchyan.packet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePacket implements Packet{
    private String message;

    @Override
    public byte getType() {
        return 0;
    }
}
