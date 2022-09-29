package com.catchyan.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static com.catchyan.packet.PacketType.ROOM_OPTION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomOptionPacket extends BasePacket{
    private int option;
    @Override
    public byte getType() {
        return ROOM_OPTION;
    }
}
