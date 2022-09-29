package com.catchyan.io;

import com.catchyan.entity.Pai;
import com.catchyan.packet.Packet;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrinterTest {
    @Test
    public void printLogo() {
        Printer.printLogo();
    }

    @Test
    public void printAnsi() throws InterruptedException {
        new Pai(Pai.Type.TONG, 1).print();
        new Pai(Pai.Type.TONG, 9).print();
        new Pai(Pai.Type.TIAO, 1).print();
        new Pai(Pai.Type.TIAO, 9).print();
        new Pai(Pai.Type.WAN, 1).print();
        new Pai(Pai.Type.WAN, 9).print();
        new Pai(Pai.Type.FENG, 1).print();
        new Pai(Pai.Type.FENG, 2).print();
        new Pai(Pai.Type.FENG, 3).print();
        new Pai(Pai.Type.FENG, 4).print();
        new Pai(Pai.Type.FENG, 5).print();
        new Pai(Pai.Type.FENG, 6).print();
        new Pai(Pai.Type.FENG, 7).print();
    }
}