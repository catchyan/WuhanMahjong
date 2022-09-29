package com.catchyan.handler;

import com.catchyan.Server;
import com.catchyan.packet.InputNamePacket;
import com.catchyan.packet.Packet;
import com.catchyan.packet.RoomOptionPacket;
import com.catchyan.io.Printer;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class ServerEventHandler_INPUT_NAME extends AbstractServerEventHandler{
    @Override
    public void handle(Channel channel, Packet packet) {
        InputNamePacket inputNamePacket = (InputNamePacket) packet;
        String username = inputNamePacket.getUsername();
        if(Server.usernameChannelMap.containsKey(username)){
            inputNamePacket.setMessage("duplicate username, please try again");
            channel.writeAndFlush(inputNamePacket);
        }else{
            Server.usernameChannelMap.put(username, channel);
            Printer.println(username + " connected to server");
            channel.attr(AttributeKey.valueOf("username")).set(username);
            channel.writeAndFlush(new RoomOptionPacket());
        }
    }

}
