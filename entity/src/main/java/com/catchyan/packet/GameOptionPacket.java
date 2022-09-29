package com.catchyan.packet;

import com.catchyan.entity.Option;
import lombok.Getter;
import lombok.Setter;

import static com.catchyan.packet.PacketType.GAME_OPTION;

@Getter
@Setter
public class GameOptionPacket extends BasePacket{
    Option option;
    @Override
    public byte getType() {
        return GAME_OPTION;
    }
}
