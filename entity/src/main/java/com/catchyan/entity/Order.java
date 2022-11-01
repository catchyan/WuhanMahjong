package com.catchyan.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    enum Type {
        TRIPLET, // 刻子
        SEQUENCE, // 顺子
        BOTH
    }
    Type type;
    Tile[] tiles;

    public Order(Type type, Tile... tiles) {
        this.type = type;
        this.tiles = tiles;
    }
}