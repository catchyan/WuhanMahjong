package com.catchyan.handler.gamehandler;

import com.catchyan.entity.Option;
import com.catchyan.entity.Pai;
import com.catchyan.entity.Person;
import com.catchyan.entity.Room;

import java.util.Optional;

public class GameOptionHandler_PLAY implements GameOptionHandler{
    @Override
    public void handle(Room room, Option option) {
        Person person = room.getPlayers().stream().filter(p -> p.getName().equals(option.getUsername())).findFirst().get();
        Pai pai = option.getPais().get(0);
        pai.setPlayerName(person);
        Optional<Pai> optional = person.getHand().stream().filter(p -> p.isEqual(pai)).findAny();
        if(optional.isPresent()){
            room.setPlayed(pai);
            person.getHand().remove(pai);
            room.calculateAndSendRoomInfo();
        }
    }
}
