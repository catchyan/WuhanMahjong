package com.catchyan;

import com.catchyan.protocal.PacketDecoder;
import com.catchyan.protocal.PacketEncoder;
import com.catchyan.io.Printer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Client {
    public static String username;
    public static final String prefix = "com.catchyan.handler.ClientEventHandler_";

    public static void main(String[] args) {
        Printer.printLogo();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        String host = "localhost";
        int port = 8000;
        if(args.length > 0){
            if("-h".equals(args[0])){
                host = args[1];
            }
            if("-p".equals(args[2])){
                port = Integer.parseInt(args[3]);
            }
        }

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel){
                        socketChannel
                                .pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 5, 4))
                                .addLast(new PacketDecoder())
                                .addLast(new PacketHandler())
                                .addLast(new PacketEncoder())
                                ;
                    }
                });
        bootstrap.connect(host, port);
    }


}