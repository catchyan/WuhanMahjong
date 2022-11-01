package com.catchyan.entity;

import lombok.Data;

import java.util.List;

@Data
public class Win {
    private boolean hasRascal; // 是否硬胡
    private boolean allDependsOnOthers; // 全球人
    private boolean allSameSuit; // 一色
    private boolean lastTen; // 海底捞
    private boolean afterKong; // 杠上开花
    private boolean robKong; // 抢杠
    private boolean selfDraw; // 自摸
    private boolean allTriplets; // 碰碰胡

    private Tile[] pair;
    private List<Order> orderList;

    public Win(Tile[] pair, List<Order> orderList) {
        this.pair = pair;
        this.orderList = orderList;
    }
}
