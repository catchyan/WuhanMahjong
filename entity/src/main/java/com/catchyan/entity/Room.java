package com.catchyan.entity;

import cn.hutool.core.util.RandomUtil;
import com.catchyan.packet.NotifyPacket;
import com.catchyan.packet.PersonViewPacket;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.catchyan.entity.Tile.Suit.*;

@Data
public class Room {
    List<Person> players = new ArrayList<>();
    private Tile rascal; // 癞子
    private Tile kong1; // 皮子1
    private Tile kong2; // 皮子2
    private Person dealer; // 庄
    private Person turn;
    private List<Tile> table;
    private Tile flop; // 翻出来的牌
    private Tile played; // 当前出的牌
    private int dealerCount = 0;
    private List<Option> sleepOptions = new ArrayList<>();


    public void init(){
        initTable();
        initPositionAndDealer();
        initRascalAndKong();
        initHand();
        startGame();
    }

    private void startGame() {
        turn = dealer;
        calculateAndSendRoomInfo();
    }

    public void calculateAndSendRoomInfo(){
        calculateOptions();
        sendRoomInfoToAllPlayers();
    }

    private void calculateOptions() {
        if(played != null){
            calcChow();
            calcPung();
            calcWin();
        }
        if(sleepOptions.isEmpty()){
            turn = turn.getNext();
            turn.getOptions().add(new Option(Option.Type.PLAY, turn.getName()));
        }
    }

    private void calcChow(){
        Person next = getNext(played.getPlayerName());
        List<Tile> withoutLaiAndPi = next
                .getHand()
                .stream()
                .filter(p -> !p.isEqual(rascal) && !p.isEqual(kong1) && !p.isEqual(kong2))
                .collect(Collectors.toList());
        boolean plus1 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Tile.plusPoint(played, 1, true)));
        boolean plus2 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Tile.plusPoint(played, 2, true)));
        boolean sub1 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Tile.plusPoint(played, -1, true)));
        boolean sub2 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Tile.plusPoint(played, -2, true)));
        if((plus1 && plus2) || (plus1 && sub1) || (sub1 && sub2)){
            Option chi = new Option(Option.Type.CHOW, next.getName());
            next.getOptions().add(chi);
            chi.setPriority(Option.Type.CHOW.getPriority());
            sleepOptions.add(chi);
        }
    }

    private void calcPung(){
        getOthers()
                .stream()
                .filter(p->p.getHand().stream().filter(pai -> pai.isEqual(played)).count() >= 2)
                .forEach(p-> {
                    Option option = new Option(Option.Type.PUNG, p.getName());
                    p.getOptions().add(option);
                    sleepOptions.add(option);
                });
    }

    private void calcWin(){
        for (Person other : getOthers()) {
            List<Tile> hand = other.getHand();
            if(other.getEat().isEmpty()){
                continue;
            }
            if(hand.contains(kong1) || hand.contains(kong2) || hand.contains(new Tile(WIND, 5))){
                continue;
            }
            List<Tile> clone = new ArrayList<>(hand);
            clone.add(played);
            List<Tile[]> pair = findPair(clone);
            if(pair.isEmpty()){
                continue;
            }
            for (Tile[] j : pair) {
                List<Tile> withoutPair = new ArrayList<>(clone);
                withoutPair.remove(j[0]);
                withoutPair.remove(j[1]);
                List<List<Order>> allOrders = findAllOrders(withoutPair);
                if(allOrders.isEmpty()){
                    continue;
                }
                // found a possible Pair(maybe not 2 5 8) and a valid list of orders
                // now check if all order hits a big Win(which not require 2 5 8 Jiang)
                // a pair of rascal can be regarded as a valid Pair
                for (List<Order> orders : allOrders) {
                    Win win = new Win(j, orders);
                    if(checkSameSuit(j, orders, other.getEat())){
                        win.setAllSameSuit(true);
                    }else{
//                        j[0].getPoint()
                    }
                }
            }
        }
    }

    private boolean checkSameSuit(Tile[] pair, List<Order> orders, List<Order> eat) {
        if(pair[0].equals(rascal)){
            return isSameSuit(eat.get(0).getTiles()[0].getSuit(), orders) && isSameSuit(eat.get(0).getTiles()[0].getSuit(), eat);
        }else{
            return isSameSuit(pair[0].getSuit(), orders) && isSameSuit(pair[0].getSuit(), eat);
        }
    }

    private boolean isSameSuit(Tile.Suit suit, List<Order> orders){
        return orders.stream()
                .map(Order::getTiles)
                .allMatch(tiles -> tiles[0].getSuit().equals(suit) && tiles[1].getSuit().equals(suit) && tiles[2].getSuit().equals(suit));
    }

    private void sendRoomInfoToAllPlayers() {
        players.forEach(player->{
            PersonViewPacket packet = new PersonViewPacket();
            packet.setNext(toPersonView(player.getNext()));
            packet.setOpposite(toPersonView(player.getNext().getNext()));
            packet.setPrev(toPersonView(player.getNext().getNext().getNext()));
            packet.setRascal(rascal);
            packet.setEat(player.getEat());
            packet.setGang(player.getKong());
            packet.setLeftPaiCount(table.size());
            packet.setHand(player.getHand());
            packet.setKong1(kong1);
            packet.setKong2(kong2);
            packet.setOptions(player.getOptions());
            player.getChannel().writeAndFlush(packet);
        });
    }

    private PersonView toPersonView(Person person){
        PersonView result = new PersonView();
        result.setName(person.getName());
        result.setHandCount(person.getHand().size());
        result.setDealer(dealer.equals(person));
        result.setEat(person.getEat());
        result.setKong(person.getKong());
        return result;
    }
    public boolean isFull(){
        return players.size() == 4;
    }

    public void addPerson(Person p){
        players.add(p);
    }

    public void notifyAll(String message){
        NotifyPacket notifyPacket = new NotifyPacket();
        notifyPacket.setMessage(message);
        players.forEach(person -> person.getChannel().writeAndFlush(notifyPacket));
    }


    private void initRascalAndKong() {
        flop = getPai(1).get(0);
        if(flop.getSuit().equals(WIND)){
            if(flop.getPoint() == 4 || flop.getPoint() == 5){
                rascal = new Tile(WIND, 6);
                kong1 = new Tile(WIND, 3);
                kong2 = new Tile(WIND, 4);
            }else if(flop.getPoint() == 6){
                rascal = new Tile(WIND, 7);
                kong1 = new Tile(WIND, 3);
                kong2 = new Tile(WIND, 4);
            } else {
                rascal = Tile.plusPoint(flop, 1, false);
                kong1 = Tile.plusPoint(flop, -1, false);
                kong2 = flop;
            }
        }else{
            rascal = Tile.plusPoint(flop, 1, false);
            kong1 = Tile.plusPoint(flop, -1, false);
            kong2 = flop;
        }
    }


    private void initHand() {
        dealer.setHand(getPai(14)).getNext()
                .setHand(getPai(13)).getNext()
                .setHand(getPai(13)).getNext()
                .setHand(getPai(13));
    }

    private void initPositionAndDealer() {
        if(dealerCount % 16 == 0 ){ // 4圈一个风 换位置
            if(dealerCount != 0){
                notifyAll("finish a 'FENG', players position change");
            }
            // 换位置
            Collections.shuffle(players);
            dealer = players.get(0);
            dealer.setNext(players.get(1)).setNext(players.get(2)).setNext(players.get(3)).setNext(dealer);
            dealer.setPrev(players.get(3)).setPrev(players.get(2)).setPrev(players.get(1)).setPrev(dealer);
            dealer.setDealer(true);
        } else{
            Person winner = findWinner();
            dealer = findDealer();
            if(winner != null && !winner.equals(dealer)){
                // 下庄
                winner.setDealer(false);
                dealer = winner.getNext();
                dealer.setDealer(true);
                dealerCount++;
            }
        }
    }

    private void initTable() {
        table = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 4; j++) {
                table.add(new Tile(DOT, i));
                table.add(new Tile(BAMBOO, i));
                table.add(new Tile(CHARACTER, i));
                if(i <= 7) {
                    // 风
                    table.add(new Tile(WIND, i));
                }
            }
        }
    }

    private Person findWinner(){
        return players.stream().filter(Person::isWinner).findAny().orElse(null);
    }
    private Person findDealer(){
        return players.stream().filter(Person::isDealer).findAny().orElse(null);
    }

    private List<Tile> getPai(int size){
        List<Tile> result = RandomUtil.randomEleList(table, size);
        result.forEach(result::remove);
        if(size > 1){
            Collections.sort(result);
        }
        return result;
    }

    private List<Person> getOthers(){
        return players.stream()
                .filter(p->!p.getName().equals(played.getPlayerName()))
                .collect(Collectors.toList());
    }

    private Person getNext(String name){
        return getPerson(name).getNext();
    }
    private Person getPerson(String name){
        return players.stream()
                .filter(p->p.getName().equals(name))
                .findFirst().orElse(null);
    }

    public List<Tile[]> findPair(List<Tile> list){
        List<Tile[]> result = new ArrayList<>();
        long rascalCount = list.stream().filter(p->p.equals(rascal)).count();
        list.stream().filter(p->!p.equals(rascal)).distinct().forEach(
                pai ->{
                    long count = list.stream().filter(tile -> tile.equals(pai)).count();
                    if(count >= 2){
                        result.add(new Tile[]{pai, pai});
                    }
                    if(rascalCount > 0){
                        result.add(new Tile[]{pai, rascal});
                    }
                }
        );
        return result;
    }

    /**
     *
     * @param list assume Pair is removed from list
     * @return all possible orders
     */
    public List<List<Order>> findAllOrders(List<Tile> list){
        // HU = all pai which is not rascal must in a correct order
        List<Tile> withoutLai = list.stream()
                .filter(p -> !p.isEqual(rascal))
                .sorted()
                .collect(Collectors.toList());
        long rascalCount = list.size() - withoutLai.size();
        List<List<Order>> result = new ArrayList<>();
        List<Order> orders;
        if(rascalCount == list.size()){
            orders = new ArrayList<>();
            orders.add(new Order(Order.Type.BOTH, rascal, rascal, rascal));
        }else{
            orders = findPossibleOrderByPai(withoutLai.get(0), list, rascalCount);
        }

        if(orders.isEmpty()){
            return result;
        }
        if(list.size() == 3){
            for (Order order : orders) {
                List<Order> single = new ArrayList<>();
                single.add(order);
                result.add(single);
            }
        }else{
            for (Order order : orders) {
                List<Tile> clone = new ArrayList<>(list);
                removeOrderFromList(order, clone);
                List<List<Order>> leftOrders = findAllOrders(clone);
                for (List<Order> leftOrder : leftOrders) {
                    leftOrder.add(order);
                }
                result.addAll(leftOrders);
            }
        }
        return result;
    }

    public void removeOrderFromList(Order order, List<Tile> list){
        for (Tile tile : order.getTiles()) {
            list.remove(tile);
        }
    }
    /**
     * @param tile tile to be found
     * @param list assume list is in correct order and has no rascal
     * @param rascalCount origin list rascal count
     * @return all possible order which contains “tile”
     */
    public List<Order> findPossibleOrderByPai(Tile tile, List<Tile> list, long rascalCount){
        List<Order> possibleOrder = new ArrayList<>();
        long count = list.stream().filter(p -> p.equals(tile)).count();
        Tile plus1 = Tile.plusPoint(tile, 1, true);
        Tile plus2 = Tile.plusPoint(tile, 2, true);
        long plus1count = 0;
        long plus2count = 0;
        if(plus1 != null && !plus1.equals(rascal)){
            plus1count = list.stream().filter(p -> p.equals(plus1)).count();
        }
        if(plus2 != null && !plus2.equals(rascal)){
            plus2count = list.stream().filter(p -> p.equals(plus2)).count();
        }
        if(count > 2){
            possibleOrder.add(new Order(Order.Type.TRIPLET, tile, tile, tile));
        }
        if(plus1count > 0 && plus2count > 0){
            possibleOrder.add(new Order(Order.Type.SEQUENCE, tile, plus1, plus2));
        }
        if(rascalCount > 0){
            if(rascalCount > 1){
                possibleOrder.add(new Order(Order.Type.BOTH, tile, rascal, rascal));
            }
            if(plus1count > 0){
                possibleOrder.add(new Order(Order.Type.SEQUENCE, tile, plus1, rascal));
            }
            if(plus2count > 0){
                possibleOrder.add(new Order(Order.Type.SEQUENCE, tile, rascal, plus2));
            }
        }
        return possibleOrder;
    }
}
