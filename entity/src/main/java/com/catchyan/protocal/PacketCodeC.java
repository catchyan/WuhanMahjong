package com.catchyan.protocal;

import com.alibaba.fastjson.JSONObject;
import com.catchyan.packet.*;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {
    private static final int MAGIC_NUMBER = 0xfacedead;
    public static final PacketCodeC INSTANCE = new PacketCodeC();
    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private PacketCodeC(){
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(PacketType.INPUT_NAME, InputNamePacket.class);
        packetTypeMap.put(PacketType.ROOM_OPTION, RoomOptionPacket.class);
        packetTypeMap.put(PacketType.JOIN_ROOM, JoinRoomPacket.class);
        packetTypeMap.put(PacketType.NOTIFY, NotifyPacket.class);
        packetTypeMap.put(PacketType.PERSON_VIEW, PersonViewPacket.class);
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet){
        byte[] bytes = JSONObject.toJSONBytes(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getType());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf){
        // 跳过magicNumber
        byteBuf.skipBytes(4);
        byte packetType = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> type = packetTypeMap.get(packetType);
        return JSONObject.parseObject(bytes, type);
    }
}
