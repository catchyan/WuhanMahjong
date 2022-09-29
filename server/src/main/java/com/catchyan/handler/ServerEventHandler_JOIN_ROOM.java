package com.catchyan.handler;

import com.catchyan.Server;
import com.catchyan.entity.Person;
import com.catchyan.entity.Room;
import com.catchyan.packet.*;
import com.catchyan.io.Printer;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class ServerEventHandler_JOIN_ROOM extends AbstractServerEventHandler{
    @Override
    public void handle(Channel channel, Packet packet) {
        JoinRoomPacket joinRoomPacket = (JoinRoomPacket) packet;
        int roomId = joinRoomPacket.getRoomId();
        if(Server.roomMap.containsKey(roomId)){
            Room room = Server.roomMap.get(roomId);
            synchronized (room){
                if(room.isFull()){
                    RoomOptionPacket roomOptionPacket = new RoomOptionPacket();
                    roomOptionPacket.setMessage("room " + roomId + " is full");
                    channel.writeAndFlush(roomOptionPacket);
                }else{
                    Person p = new Person();
                    String username = channel.attr(AttributeKey.valueOf("username")).get().toString();
                    p.setChannel(channel);
                    p.setName(username);
                    room.addPerson(p);
                    room.notifyAll(username + " joined room");
                    channel.attr(AttributeKey.valueOf("roomId")).set(roomId);
                    if(room.isFull()){
                        // start game
                        Printer.print("room " + roomId + " game start");
                        room.notifyAll("game start! good luck, have fun!");
                        room.init();
                    }else{
                        NotifyPacket msg = new NotifyPacket();
                        msg.setMessage("join room success, waiting other players join in");
                        channel.writeAndFlush(msg);
                    }
                }
            }
        }else{
            RoomOptionPacket roomOptionPacket = new RoomOptionPacket();
            roomOptionPacket.setMessage("room id " + roomId + " is not exist");
            channel.writeAndFlush(roomOptionPacket);
        }
    }
}
