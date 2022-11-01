package com.catchyan.entity;

import lombok.Data;

import java.util.List;

@Data
public class PersonView {
    private String name;
    private boolean isDealer;
    private int handCount;
    private List<Order> eat;
    private List<Tile> kong;
}
