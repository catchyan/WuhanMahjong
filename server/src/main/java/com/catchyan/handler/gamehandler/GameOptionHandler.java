package com.catchyan.handler.gamehandler;

import com.catchyan.entity.Option;
import com.catchyan.entity.Room;
import com.catchyan.handler.AbstractServerEventHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface GameOptionHandler {
    Map<Option.Type, GameOptionHandler> handlers = new ConcurrentHashMap<>();
    void handle(Room room, Option option);
    static GameOptionHandler getHandler(Option.Type type){
        if(handlers.containsKey(type)){
            return handlers.get(type);
        }
        Class<?> handlerClass;
        GameOptionHandler handler;
        try {
            handlerClass = Class.forName("com.catchyan.handler.gamehandler.GameOptionHandler_" + type.name());
            handler = (GameOptionHandler) handlerClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("handler not found");
        }
        handlers.put(type, handler);
        return handler;
    }
}
