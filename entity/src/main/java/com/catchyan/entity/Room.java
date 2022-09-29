package com.catchyan.entity;

import cn.hutool.core.util.RandomUtil;
import com.catchyan.packet.NotifyPacket;
import com.catchyan.packet.PersonViewPacket;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.catchyan.entity.Pai.Type.*;

@Data
public class Room {
    List<Person> players = new ArrayList<>();
    private Pai lai;
    private Pai pi1;
    private Pai pi2;
    private Person dealer;
    private Person turn;
    private List<Pai> table;
    private Pai flop; // 翻出来的牌

    private Pai played; // 当前出的牌
    private int zhuangCount = 0;

    private List<Option> sleepOptions = new ArrayList<>();


    public void init(){
        initTable();
        initPositionAndDealer();
        initLaiZiAndPiZi();
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
            calcChi();
            calcPeng();
            calcHu();
        }
        if(sleepOptions.isEmpty()){
            turn = turn.getNext();
            turn.getOptions().add(new Option(Option.Type.PLAY, turn.getName()));
        }
    }

    private void calcChi(){
        Person next = getNext(played.getPlayerName());
        List<Pai> withoutLaiAndPi = next
                .getHand()
                .stream()
                .filter(p -> !p.isEqual(lai) && !p.isEqual(pi1) && !p.isEqual(pi2))
                .collect(Collectors.toList());
        boolean plus1 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Pai.plusPoint(played, 1, true)));
        boolean plus2 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Pai.plusPoint(played, 2, true)));
        boolean sub1 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Pai.plusPoint(played, -1, true)));
        boolean sub2 = withoutLaiAndPi.stream().anyMatch(p -> p.isEqual(Pai.plusPoint(played, -2, true)));
        if((plus1 && plus2) || (plus1 && sub1) || (sub1 && sub2)){
            Option chi = new Option(Option.Type.CHI, next.getName());
            next.getOptions().add(chi);
            chi.setPriority(Option.Type.CHI.getPriority());
            sleepOptions.add(chi);
        }
    }

    private void calcPeng(){
        getOthers(played.getPlayerName())
                .stream()
                .filter(p->p.getHand().stream().filter(pai -> pai.isEqual(played)).count() >= 2)
                .forEach(p-> {
                    Option option = new Option(Option.Type.PENG, p.getName());
                    p.getOptions().add(option);
                    sleepOptions.add(option);
                });
    }

    private void calcHu(){
        for (Person other : getOthers(played.getPlayerName())) {
            List<Pai> clone = new ArrayList<>(other.getHand());
            clone.add(played);
            List<Pai[]> jiang = findJiang(clone, lai);
            jiang.forEach(j->{
                List<Pai> withoutJiang = new ArrayList<>(clone);
                withoutJiang.remove(j[0]);
                withoutJiang.remove(j[1]);
                List<List<Order>> allOrders = findAllOrders(withoutJiang, lai);
                if(allOrders.isEmpty()){
                    return;
                }
                List<Hu> possibleHu = new ArrayList<>();
                for (List<Order> orders : allOrders) {
                    Hu hu = new Hu(j, orders);
                    possibleHu.add(hu);
                }
            });
        }
    }

    private void sendRoomInfoToAllPlayers() {
        players.forEach(player->{
            PersonViewPacket packet = new PersonViewPacket();
            packet.setNext(toPersonView(player.getNext()));
            packet.setOpposite(toPersonView(player.getNext().getNext()));
            packet.setPrev(toPersonView(player.getNext().getNext().getNext()));
            packet.setLai(lai);
            packet.setKou(player.getKou());
            packet.setGang(player.getGang());
            packet.setLeftPaiCount(table.size());
            packet.setHand(player.getHand());
            packet.setPi1(pi1);
            packet.setPi2(pi2);
            packet.setOptions(player.getOptions());
            player.getChannel().writeAndFlush(packet);
        });
    }

    private PersonView toPersonView(Person person){
        PersonView result = new PersonView();
        result.setName(person.getName());
        result.setHandCount(person.getHand().size());
        result.setDealer(dealer.equals(person));
        result.setKou(person.getKou());
        result.setGang(person.getGang());
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


    private void initLaiZiAndPiZi() {
        flop = getPai(1).get(0);
        if(flop.getType().equals(FENG)){
            if(flop.getPoint() == 4 || flop.getPoint() == 5){
                // 翻出北 中, 发财癞子
                lai = new Pai(FENG, 6);
                // 西 北皮
                pi1 = new Pai(FENG, 3);
                pi2 = new Pai(FENG, 4);
            }else if(flop.getPoint() == 6){
                // 翻出发财，白班癞子
                lai = new Pai(FENG, 7);
                pi1 = new Pai(FENG, 3);
                pi2 = new Pai(FENG, 4);
            } else {
                lai = Pai.plusPoint(flop, 1, false);
                pi1 = Pai.plusPoint(flop, -1, false);
                pi2 = flop;
            }
        }else{
            lai = Pai.plusPoint(flop, 1, false);
            pi1 = Pai.plusPoint(flop, -1, false);
            pi2 = flop;
        }
    }


    private void initHand() {
        dealer.setHand(getPai(14)).getNext()
                .setHand(getPai(13)).getNext()
                .setHand(getPai(13)).getNext()
                .setHand(getPai(13));
    }

    private void initPositionAndDealer() {
        if(zhuangCount % 16 == 0 ){ // 4圈一个风 换位置
            if(zhuangCount != 0){
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
                zhuangCount ++;
            }
        }
    }

    private void initTable() {
        table = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 4; j++) {
                table.add(new Pai(TONG, i));
                table.add(new Pai(TIAO, i));
                table.add(new Pai(WAN, i));
                if(i <= 7) {
                    // 风
                    table.add(new Pai(FENG, i));
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

    private List<Pai> getPai(int size){
        List<Pai> result = RandomUtil.randomEleList(table, size);
        result.forEach(result::remove);
        if(size > 1){
            Collections.sort(result);
        }
        return result;
    }

    private List<Person> getOthers(String name){
        return players.stream()
                .filter(p->!p.getName().equals(played.getPlayerName()))
                .collect(Collectors.toList());
    }

    private Person getNext(String name){
        return getPerson(name).getNext();
    }
    private Person getPerson(String name){
        return players.stream()
                .filter(p->p.getName().equals(played.getPlayerName()))
                .findFirst().get();
    }

    public List<Pai[]> findJiang(List<Pai> list, Pai lai){
        List<Pai[]> result = new ArrayList<>();
        long laiCount = list.stream().filter(p->p.equals(lai)).count();
        list.stream().filter(p->!p.equals(lai)).distinct().forEach(
                pai ->{
                    long count = list.stream().filter(pai1 -> pai1.equals(pai)).count();
                    if(count >= 2){
                        result.add(new Pai[]{pai, pai});
                    }
                    if(laiCount > 0){
                        result.add(new Pai[]{pai, lai});
                    }
                }
        );
        return result;
    }

    /**
     *
     * @param list assume Jiang is removed from list
     * @param lai lai
     * @return
     */
    public List<List<Order>> findAllOrders(List<Pai> list, Pai lai){
        // HU = all pai which is not lai must in a correct order
        List<Pai> withoutLai = list.stream()
                .filter(p -> !p.isEqual(lai))
                .sorted()
                .collect(Collectors.toList());
        long laiCount = list.size() - withoutLai.size();
        List<List<Order>> result = new ArrayList<>();
        List<Order> orders;
        if(laiCount == list.size()){
            orders = new ArrayList<>();
            orders.add(new Order(Order.Type.BOTH, lai, lai, lai));
        }else{
            orders = findPossibleOrderByPai(withoutLai.get(0), list, lai, laiCount);
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
                List<Pai> clone = new ArrayList<>(list);
                removeOrderFromList(order, clone);
                List<List<Order>> leftOrders = findAllOrders(clone, lai);
                for (List<Order> leftOrder : leftOrders) {
                    leftOrder.add(order);
                }
                result.addAll(leftOrders);
            }
        }
        return result;
    }

    public List<Pai> removeOrderFromList(Order order, List<Pai> list){
        for (Pai pai : order.getPais()) {
            list.remove(pai);
        }
        return list;
    }
    /**
     * @param pai pai to be found
     * @param list assume list is in correct order and has no lai
     * @param laiCount origin list lai count
     * @return all possible order which contains “pai”
     */
    public List<Order> findPossibleOrderByPai(Pai pai, List<Pai> list, Pai lai, long laiCount){
        List<Order> possibleOrder = new ArrayList<>();
        long count = list.stream().filter(p -> p.equals(pai)).count();
        Pai plus1 = Pai.plusPoint(pai, 1, true);
        Pai plus2 = Pai.plusPoint(pai, 2, true);
        long plus1count = 0;
        long plus2count = 0;
        if(plus1 != null && !plus1.equals(lai)){
            plus1count = list.stream().filter(p -> p.equals(plus1)).count();
        }
        if(plus2 != null && !plus2.equals(lai)){
            plus2count = list.stream().filter(p -> p.equals(plus2)).count();
        }
        if(count > 2){
            possibleOrder.add(new Order(Order.Type.KE, pai, pai, pai));
        }
        if(plus1count > 0 && plus2count > 0){
            possibleOrder.add(new Order(Order.Type.SHUN, pai, plus1, plus2));
        }
        if(laiCount > 0){
            if(laiCount > 1){
                possibleOrder.add(new Order(Order.Type.BOTH, pai, lai, lai));
            }
            if(plus1count > 0){
                possibleOrder.add(new Order(Order.Type.SHUN, pai, plus1, lai));
            }
            if(plus2count > 0){
                possibleOrder.add(new Order(Order.Type.SHUN, pai, lai, plus2));
            }
        }
        return possibleOrder;
    }
}
