package com.catchyan.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.catchyan.packet.PacketType.INPUT_NAME;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputNamePacket extends BasePacket{
    private String username;
    @Override
    public byte getType() {
        return INPUT_NAME;
    }
}
