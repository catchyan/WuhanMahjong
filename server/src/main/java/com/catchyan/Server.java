package com.catchyan;

import com.catchyan.entity.Room;
import com.catchyan.handler.ServerEventHandler;
import com.catchyan.packet.PacketType;
import com.catchyan.protocal.PacketDecoder;
import com.catchyan.protocal.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    public static final String prefix = "com.catchyan.handler.ServerEventHandler_";
    public static final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();
    public static final Map<Integer, Room> roomMap = new ConcurrentHashMap<>();
    public static AtomicInteger roomId = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("init server...");
        // 初始化事件处理器
        int port = 8000;
        if(args.length > 0 && "-p".equals(args[0])){
            port = Integer.parseInt(args[1]);
        }
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel
                                .pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 5, 4))
                                .addLast(new PacketDecoder())
                                .addLast(new ServerHandler())
                                .addLast(new PacketEncoder())
                                ;
                    }
                });
        int finalPort = port;
        serverBootstrap.bind(port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("server init success on port " + finalPort);
            }else{
                System.out.println(finalPort + " is in used, server start fail");
            }
        });
    }
}
