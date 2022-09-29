package com.catchyan.handler;

import com.catchyan.Server;
import com.catchyan.entity.Option;
import com.catchyan.entity.Person;
import com.catchyan.entity.Room;
import com.catchyan.handler.gamehandler.GameOptionHandler;
import com.catchyan.packet.GameOptionPacket;
import com.catchyan.packet.Packet;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class ServerEventHandler_GAME_OPTION extends AbstractServerEventHandler{
    @Override
    public void handle(Channel channel, Packet packet) {
        GameOptionPacket optionPacket = (GameOptionPacket) packet;
        Integer roomId = Integer.valueOf(channel.attr(AttributeKey.valueOf("roomId")).get().toString());
        String username = channel.attr(AttributeKey.valueOf("username")).get().toString();
        Option option = optionPacket.getOption();
        option.setUsername(username);
        Room room = Server.roomMap.get(roomId);
        GameOptionHandler.getHandler(option.getType()).handle(room, option);
    }
}
