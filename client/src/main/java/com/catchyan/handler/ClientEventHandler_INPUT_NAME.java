package com.catchyan.handler;

import com.catchyan.packet.*;
import com.catchyan.io.Printer;
import com.catchyan.io.Reader;
import io.netty.channel.Channel;

public class ClientEventHandler_INPUT_NAME extends AbstractClientEventHandler {
    @Override
    public void handle(Channel channel, Packet packet) {
        Printer.print("please input your name:");
        String name = Reader.read();
        channel.writeAndFlush(new InputNamePacket(name));
    }
}
