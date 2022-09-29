package com.catchyan.packet;

import static com.catchyan.packet.PacketType.NOTIFY;

public class NotifyPacket extends BasePacket{
    @Override
    public byte getType() {
        return NOTIFY;
    }
}
