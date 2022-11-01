package com.catchyan.entity;

import com.catchyan.io.Printer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Option {
    @AllArgsConstructor
    @Getter
    public enum Type{
        CHOW(1, "chi[c/chi]"),
        PUNG(5, "peng[peng]"),
        GONG(5, "gang[g/gang]"),
        WIN(9, "hu[h/hu]"),
        PASS(0, "pass[p/pass]"),
        PLAY(0, "play[d/da]"),
        ;
        private final int priority;
        private final String display;
    }
    private Type type;

    private List<Tile> tiles = new ArrayList<>();

    int priority;

    String username;

    public Option(Type type){
        this.type = type;
    }

    public Option(Type type, String username){
        this.type = type;
        this.username = username;
    }

    public Option(Type type, String username, int priority){
        this.type = type;
        this.username = username;
        this.priority = priority;
    }

    public void print(){
        Printer.print(this.getType().getDisplay());
    }
}
