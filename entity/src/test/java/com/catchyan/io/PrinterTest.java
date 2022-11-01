package com.catchyan.io;

import com.catchyan.entity.Tile;
import org.junit.Test;

public class PrinterTest {
    @Test
    public void printLogo() {
        Printer.printLogo();
    }

    @Test
    public void printAnsi() throws InterruptedException {
        new Tile(Tile.Suit.DOT, 1).print();
        new Tile(Tile.Suit.DOT, 9).print();
        new Tile(Tile.Suit.BAMBOO, 1).print();
        new Tile(Tile.Suit.BAMBOO, 9).print();
        new Tile(Tile.Suit.CHARACTER, 1).print();
        new Tile(Tile.Suit.CHARACTER, 9).print();
        new Tile(Tile.Suit.WIND, 1).print();
        new Tile(Tile.Suit.WIND, 2).print();
        new Tile(Tile.Suit.WIND, 3).print();
        new Tile(Tile.Suit.WIND, 4).print();
        new Tile(Tile.Suit.WIND, 5).print();
        new Tile(Tile.Suit.WIND, 6).print();
        new Tile(Tile.Suit.WIND, 7).print();
    }
}