package com.catchyan;

import com.catchyan.handler.AbstractServerEventHandler;
import com.catchyan.io.Printer;
import com.catchyan.packet.InputNamePacket;
import com.catchyan.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ServerHandler extends SimpleChannelInboundHandler<Packet> {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String username = ctx.channel().attr(AttributeKey.valueOf("username")).get().toString();
        Printer.println(username + " closed connection");
        Server.usernameChannelMap.remove(username);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        AbstractServerEventHandler.getHandler(msg.getType()).showMessageAndHandle(ctx.channel(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new InputNamePacket());
    }
}
