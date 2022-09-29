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
        CHI(1, "chi[c/chi]"), // 吃
        PENG(5, "peng[peng]"), // 碰
        GANG(5, "gang[g/gang]"), // 杠
        HU(9, "hu[h/hu]"), // 胡
        PASS(0, "pass[p/pass]"), // 过
        PLAY(0, "play[d/da]"), // 打牌
        ;
        private final int priority;
        private final String display;
    }
    private Type type;

    private List<Pai> pais = new ArrayList<>();

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
