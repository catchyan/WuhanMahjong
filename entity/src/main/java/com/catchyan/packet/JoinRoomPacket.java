package com.catchyan.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.catchyan.packet.PacketType.JOIN_ROOM;

@AllArgsConstructor
@Getter
@Setter
public class JoinRoomPacket extends BasePacket{
    private int roomId;
    @Override
    public byte getType() {
        return JOIN_ROOM;
    }
}
