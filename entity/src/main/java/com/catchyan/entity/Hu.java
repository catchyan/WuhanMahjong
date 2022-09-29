package com.catchyan.entity;

import lombok.Data;

import java.util.List;

@Data
public class Hu {
    private boolean hasLaiZi; // 是否硬胡
    private boolean isOnlyJiang; // 全球人
    private boolean isSameType; // 一色
    private boolean isLastTen; // 海底捞
    private boolean isGangHu; // 杠上开花
    private boolean robGang; // 抢杠
    private boolean isZiMo; // 自摸
    private boolean isPengPengHu; // 碰碰胡

    private Pai[] Jiang;
    private List<Order> orderList;

    public Hu(Pai[] jiang, List<Order> orderList) {
        Jiang = jiang;
        this.orderList = orderList;
    }
}
