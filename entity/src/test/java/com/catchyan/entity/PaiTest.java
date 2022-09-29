package com.catchyan.entity;

import com.catchyan.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.catchyan.entity.Pai.Type.*;

public class PaiTest {

    @Test
    public void plusPoint() {
        System.out.println(Pai.plusPoint(new Pai(FENG, 7), 2, true));
    }


    @Test
    public void print(){
        List<Pai> list = new ArrayList<>();
        list.add(new Pai(WAN, 1));
        list.add(new Pai(WAN, 1));
        list.add(new Pai(WAN, 1));
        list.add(new Pai(WAN, 1));
        list.add(new Pai(WAN, 1));
        list.add(new Pai(WAN, 1));
        List<Pai> toberemove = new ArrayList<>();
        toberemove.add(new Pai(WAN, 1));
        list.removeAll(toberemove);
        System.out.println("123");
    }

    @Test
    public void test(){
        Pai pai1 = new Pai(WAN, 1);
        Pai pai2 = new Pai(WAN, 1);
        System.out.println(pai1.isEqual(pai2) );
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
            System.out.println(matcher.group(2)); // pais
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
        Map<Pai, Integer> map = new HashMap<>();
        Pai p1 = new Pai(TONG, 1);
        Pai p2 = new Pai(TONG, 1);
        map.put(p1, 1);
        map.put(p2, 2);
        map.get(new Pai(TONG, 1));
    }

    @Test
    public void removeFromList(){
        List<Pai> list = new ArrayList<>();
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.remove(new Pai(TONG, 1));
    }

    @Test
    public void findJiang(){
        List<Pai> list = new ArrayList<>();
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 3));
        list.add(new Pai(TONG, 2));
//        System.out.println(Room.findJiang(list, new Pai(TONG, 2)));
    }

    @Test
    public void findAllOrders(){
        List<Pai> list = new ArrayList<>();
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 1));
        list.add(new Pai(TONG, 2));
        list.add(new Pai(TONG, 2));
        list.add(new Pai(TONG, 2));
        list.add(new Pai(TONG, 3));
        list.add(new Pai(TONG, 3));
        list.add(new Pai(TONG, 4));

        Pai lai = new Pai(TONG, 3);
//        Room.findAllOrders(list, lai);
    }
}