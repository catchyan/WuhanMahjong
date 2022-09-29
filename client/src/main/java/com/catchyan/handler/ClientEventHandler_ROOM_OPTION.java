package com.catchyan.handler;

import com.catchyan.packet.JoinRoomPacket;
import com.catchyan.packet.Packet;
import com.catchyan.packet.RoomOptionPacket;
import com.catchyan.io.Printer;
import com.catchyan.io.Reader;
import io.netty.channel.Channel;

public class ClientEventHandler_ROOM_OPTION extends AbstractClientEventHandler {
    @Override
    public void handle(Channel channel, Packet packet) {
        Printer.println("choose an option:\n1.create room\n2.room list\n3.join room");
        String option = Reader.read();
        if("1".equals(option) || "2".equals(option)) {
            channel.writeAndFlush(new RoomOptionPacket(Integer.parseInt(option)));
        }else if("3".equals(option)){
            Printer.print("input room id:");
            int roomId = Reader.readInt();
            channel.writeAndFlush(new JoinRoomPacket(roomId));
        }else{
            Printer.println("invalid input, try again.");
            handle(channel, packet);
        }
    }
}
