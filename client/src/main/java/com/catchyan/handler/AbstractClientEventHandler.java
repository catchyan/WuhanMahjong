package com.catchyan.handler;

import cn.hutool.core.util.StrUtil;
import com.catchyan.packet.Packet;
import com.catchyan.packet.PacketType;
import com.catchyan.io.Printer;
import io.netty.channel.Channel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractClientEventHandler implements ClientEventHandler{
    static Map<Byte, AbstractClientEventHandler> handlers = new ConcurrentHashMap<>();
    public void showMessageAndHandle(Channel channel, Packet packet){
        if(packet != null && StrUtil.isNotEmpty(packet.getMessage())){
            Printer.println(packet.getMessage());
        }
        handle(channel, packet);
    }
    public static AbstractClientEventHandler getHandler(byte packetType){
        if(handlers.containsKey(packetType)){
            return handlers.get(packetType);
        }
        Optional<Field> any = Arrays.stream(PacketType.class.getDeclaredFields()).filter(field -> {
            try {
                return field.get(null).equals(packetType);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).findAny();
        Class<?> handlerClass;
        AbstractClientEventHandler handler = null;
        if(any.isPresent()){
            String name = any.get().getName();
            try {
                handlerClass = Class.forName("com.catchyan.handler.ClientEventHandler_" + name);
                handler = (AbstractClientEventHandler) handlerClass.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("handler not found");
            }
            handlers.put(packetType, handler);
        }
        return handler;
    }
}
