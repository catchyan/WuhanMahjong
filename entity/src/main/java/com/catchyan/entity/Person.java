package com.catchyan.entity;

import io.netty.channel.Channel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Person {
    private String name;        // 姓名
    private List<Tile> hand;     // 手牌
    private List<Order> eat = new ArrayList<>();      // 开口区
    private List<Tile> kong = new ArrayList<>();     // 杠区
    private boolean isWinner;   // 是否为赢家
    private boolean isDealer;   // 是否为庄
    private Channel channel;
    private int multiple; // 番数
    private Person prev;        // 上家
    private Person next;        // 下家
    private List<Option> options = new ArrayList<>(); // 玩家可操作列表

    public Person setNext(Person next){
        this.next = next;
        return next;
    }

    public Person setPrev(Person prev){
        this.prev = prev;
        return prev;
    }

    public Person setHand(List<Tile> hand){
        this.hand = hand;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}