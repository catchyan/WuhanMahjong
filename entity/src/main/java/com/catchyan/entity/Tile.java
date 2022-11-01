package com.catchyan.entity;

import com.catchyan.io.Printer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Tile implements Comparable<Tile> {
    @AllArgsConstructor
    @Getter
    public enum Suit {
        DOT(0), // 筒 dot
        BAMBOO(1), // 条 bamboo
        CHARACTER(2), // 万 character
        WIND(3); // 风 in Wuhan Mahjong 發 中 白(white) is regarded as WIND
        private final int offset; // the offset in Tile resource file
    }

    private Suit suit;
    private int point; // 1-9  WIND: 1.东 2.南 3.西 4.北 5.中 6.发 7.白(WHITE)
    private String playerName;
    private boolean visible = false;

    public boolean isEqual(Tile tile){
        if(tile == null){
            return false;
        }
        return this.suit == tile.getSuit() && this.point == tile.getPoint();
    }

    @Override
    public int compareTo(Tile o) {
        if(this.getSuit() == o.getSuit()){
            return Integer.compare(this.getPoint(), o.getPoint());
        }else{
            return this.getSuit().compareTo(o.getSuit());
        }
    }

    public Tile(Suit suit, int point) {
        this.suit = suit;
        this.point = point;
    }

    public Tile(Suit suit) {
        this.suit = suit;
    }

    public Tile() {

    }

    /**
     * add Tile point
     * @param tile to be added
     * @param plusBy plus by
     * @param strict is order
     * @return a new instance of tile which point is added by "plusBy"
     */
    public static Tile plusPoint(Tile tile, int plusBy, boolean strict){
        int point = tile.getPoint() - 1 + plusBy;
        if(tile.suit.equals(Suit.WIND)){
            if(strict){
                return null;
            }
            return new Tile(Suit.WIND, (point) % 7 + 1);
        }else{
            if(strict){
                if(point > 9 || point < 1){
                    return null;
                }
            }
            return new Tile(tile.getSuit(), (point) % 9 + 1);
        }
    }

    public void print(){
        Printer.printPai(this, Printer.Style.SIMPLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return point == tile.point && suit == tile.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, point);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "suit=" + suit +
                ", point=" + point +
                '}';
    }
}
