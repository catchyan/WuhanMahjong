package com.catchyan.entity;

import com.catchyan.io.Printer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static com.catchyan.entity.Pai.Type.FENG;

@Getter
@Setter
public class Pai implements Comparable<Pai> {
    @AllArgsConstructor
    @Getter
    public enum Type{
        TONG(0),
        TIAO(1),
        WAN(2),
        FENG(3);
        private int offset;
    }

    private Type type;
    private int point; // 点数 1-9  风: 1.东 2.南 3.西 4.北 5.中 6.发 7.白
    private String playerName; // 出牌人
    private boolean visible = false;

    public boolean isEqual(Pai pai){
        if(pai == null){
            return false;
        }
        return this.type == pai.getType() && this.point == pai.getPoint();
    }

    @Override
    public int compareTo(Pai o) {
        if(this.getType() == o.getType()){
            return Integer.compare(this.getPoint(), o.getPoint());
        }else{
            return this.getType().compareTo(o.getType());
        }
    }

    public Pai(Type type, int point) {
        this.type = type;
        this.point = point;
    }

    public Pai(Type type) {
        this.type = type;
    }

    public Pai() {

    }

    /**
     * add Pai_Simple point
     * @param pai to be add
     * @param plusBy plus by
     * @param strict is order
     * @return
     */
    public static Pai plusPoint(Pai pai, int plusBy, boolean strict){
        int point = pai.getPoint() - 1 + plusBy;
        if(pai.type.equals(FENG)){
            if(strict){
                return null;
            }
            return new Pai(FENG, (point) % 7 + 1);
        }else{
            if(strict){
                if(point > 9 || point < 1){
                    return null;
                }
            }
            return new Pai(pai.getType(), (point) % 9 + 1);
        }
    }

    public void print(){
        Printer.printPai(this, Printer.Style.SIMPLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pai pai = (Pai) o;
        return point == pai.point && type == pai.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, point);
    }

    @Override
    public String toString() {
        return "Pai{" +
                "type=" + type +
                ", point=" + point +
                '}';
    }
}
