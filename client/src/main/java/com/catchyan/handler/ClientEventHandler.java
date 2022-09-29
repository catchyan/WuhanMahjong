package com.catchyan.handler;

import com.catchyan.packet.Packet;
import io.netty.channel.Channel;

public interface ClientEventHandler {
    void handle(Channel channel, Packet packet);
}
