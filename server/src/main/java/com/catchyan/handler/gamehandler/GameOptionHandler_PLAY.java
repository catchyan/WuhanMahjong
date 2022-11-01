package com.catchyan.handler.gamehandler;

import com.catchyan.entity.Option;
import com.catchyan.entity.Tile;
import com.catchyan.entity.Person;
import com.catchyan.entity.Room;

import java.util.Optional;

public class GameOptionHandler_PLAY implements GameOptionHandler{
    @Override
    public void handle(Room room, Option option) {
        Person person = room.getPlayers().stream().filter(p -> p.getName().equals(option.getUsername())).findFirst().get();
        Tile tile = option.getTiles().get(0);
        tile.setPlayerName(person);
        Optional<Tile> optional = person.getHand().stream().filter(p -> p.isEqual(tile)).findAny();
        if(optional.isPresent()){
            room.setPlayed(tile);
            person.getHand().remove(tile);
            room.calculateAndSendRoomInfo();
        }
    }
}
