package com.catchyan;

import com.catchyan.handler.AbstractClientEventHandler;
import com.catchyan.handler.ClientEventHandler;
import com.catchyan.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        AbstractClientEventHandler handler = AbstractClientEventHandler.getHandler(packet.getType());
        handler.showMessageAndHandle(ctx.channel(), packet);
    }
}
