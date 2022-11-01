package com.catchyan.entity;

import com.catchyan.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.catchyan.entity.Tile.Suit.*;

public class TileTest {

    @Test
    public void plusPoint() {
        System.out.println(Tile.plusPoint(new Tile(WIND, 7), 2, true));
    }


    @Test
    public void print(){
        List<Tile> list = new ArrayList<>();
        list.add(new Tile(CHARACTER, 1));
        list.add(new Tile(CHARACTER, 1));
        list.add(new Tile(CHARACTER, 1));
        list.add(new Tile(CHARACTER, 1));
        list.add(new Tile(CHARACTER, 1));
        list.add(new Tile(CHARACTER, 1));
        List<Tile> toberemove = new ArrayList<>();
        toberemove.add(new Tile(CHARACTER, 1));
        list.removeAll(toberemove);
        System.out.println("123");
    }

    @Test
    public void test(){
        Tile tile1 = new Tile(CHARACTER, 1);
        Tile tile2 = new Tile(CHARACTER, 1);
        System.out.println(tile1.isEqual(tile2) );
    }

    @Test
    public void test1(){
        Pattern compile = Pattern.compile("^(chi|c|peng|p|gang|g|hu|h|pass|da|d)((\\s\\|[1-9]|\\sw[1-9]|\\s\\.[1-9]|\\s东|\\s南|\\s西|\\s北|\\s中|\\s發|\\s白){0,2})$");
        String command = "chi";
        Matcher matcher = compile.matcher(command);
        if(matcher.matches()){
            System.out.println(matcher.groupCount());
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1)); // command
            System.out.println(matcher.group(2)); // tiles
            System.out.println(matcher.group(3));
        }
    }
    @Test
    public void test2(){
    }

    @Test
    public void command(){
        Util.getOptionByCommand("chi w1 w2");
    }

    @Test
    public void mapTest(){
        Map<Tile, Integer> map = new HashMap<>();
        Tile p1 = new Tile(DOT, 1);
        Tile p2 = new Tile(DOT, 1);
        map.put(p1, 1);
        map.put(p2, 2);
        map.get(new Tile(DOT, 1));
    }

    @Test
    public void removeFromList(){
        List<Tile> list = new ArrayList<>();
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.remove(new Tile(DOT, 1));
    }

    @Test
    public void findJiang(){
        List<Tile> list = new ArrayList<>();
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 3));
        list.add(new Tile(DOT, 2));
//        System.out.println(Room.findJiang(list, new Tile(TONG, 2)));
    }

    @Test
    public void findAllOrders(){
        List<Tile> list = new ArrayList<>();
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 1));
        list.add(new Tile(DOT, 2));
        list.add(new Tile(DOT, 2));
        list.add(new Tile(DOT, 2));
        list.add(new Tile(DOT, 3));
        list.add(new Tile(DOT, 3));
        list.add(new Tile(DOT, 4));

        Tile lai = new Tile(DOT, 3);
//        Room.findAllOrders(list, lai);
    }
}