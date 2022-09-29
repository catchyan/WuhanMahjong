package com.catchyan.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    enum Type {
        KE,
        SHUN,
        BOTH
    }
    Type type;
    Pai[] pais;

    public Order(Type type, Pai ...pais) {
        this.type = type;
        this.pais = pais;
    }
}