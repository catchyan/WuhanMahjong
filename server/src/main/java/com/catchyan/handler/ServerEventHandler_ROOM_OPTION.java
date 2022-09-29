package com.catchyan.handler;

import com.catchyan.Server;
import com.catchyan.entity.Person;
import com.catchyan.entity.Room;
import com.catchyan.packet.*;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

public class ServerEventHandler_ROOM_OPTION extends AbstractServerEventHandler{
    @Override
    public void handle(Channel channel, Packet packet) {
        RoomOptionPacket roomOptionPacket = (RoomOptionPacket) packet;
        if(roomOptionPacket.getOption() == 1){
            // create room
            Room room = new Room();
            int roomId = Server.roomId.incrementAndGet();
            Server.roomMap.put(roomId, room);
            Person p = new Person();
            p.setName(channel.attr(AttributeKey.valueOf("username")).toString());
            p.setChannel(channel);
            room.addPerson(p);
            NotifyPacket msg = new NotifyPacket();
            msg.setMessage("create room success, room id is " + roomId + ", waiting other players join in");
            channel.writeAndFlush(msg);
        }
        if(roomOptionPacket.getOption() == 2){
            // room list
            StringBuilder stringBuilder = new StringBuilder();
            Server.roomMap.forEach((integer, room) -> {
                stringBuilder.append("id:").append(integer);
                if(room.isFull()){
                    stringBuilder.append("(Full)");
                }
            });
            RoomOptionPacket msg = new RoomOptionPacket();
            msg.setMessage(stringBuilder.toString());
            channel.writeAndFlush(msg);
        }
    }
}
