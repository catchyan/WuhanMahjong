package com.catchyan.handler;

import com.catchyan.Util;
import com.catchyan.entity.Option;
import com.catchyan.entity.Order;
import com.catchyan.entity.Tile;
import com.catchyan.entity.PersonView;
import com.catchyan.io.Printer;
import com.catchyan.io.Reader;
import com.catchyan.packet.Packet;
import com.catchyan.packet.PersonViewPacket;
import io.netty.channel.Channel;

import java.util.List;


public class ClientEventHandler_PERSON_VIEW extends AbstractClientEventHandler {
    @Override
    public void handle(Channel channel, Packet packet) {
        PersonViewPacket personViewPacket = (PersonViewPacket) packet;
        print(personViewPacket);
        if(personViewPacket.getOptions().size() > 0){
            Option option = getCommand();
        }
    }

    private void print(PersonViewPacket packet) {
        printPersonView(packet.getPrev(), " (prev)");
        printPersonView(packet.getOpposite(), " (opposite)");
        printPersonView(packet.getNext(), " (next)");
        printTable(packet.getRascal(), packet.getKong1(), packet.getKong2(), packet.getLeftPaiCount());
        printSelfInfo(packet.getHand(), packet.getEat(), packet.getGang(), packet.getOptions());

    }

    private Option getCommand(){
        Printer.println("please input your command:");
        String command = Reader.read();
        Option option = Util.getOptionByCommand(command);
        if(option == null){
            Printer.println("invalid input, try again");
            return getCommand();
        }
        return option;
    }

    private void printSelfInfo(List<Tile> hand, List<Order> kou, List<Tile> gang, List<Option> options) {
        Printer.print("kou: ");
        for (Order order : kou) {
            for (Tile tile : order.getTiles()) {
                tile.print();
            }
            Printer.space();
        }
        Printer.print(" gang: ");
        Printer.nextLine();
        for (Tile tile : gang) {
            tile.print();
            Printer.space();
        }
        Printer.nextLine();
        Printer.print(" hand: ");
        for (Tile tile : hand) {
            tile.print();
        }
        if(options.size() > 0){
            Printer.print(" operation: ");
        }
        for (Option option : options) {
            option.print();
        }
    }

    private void printTable(Tile lai, Tile pi1, Tile pi2, int leftPaiCount) {
        Printer.print("lai:");
        lai.print();
        Printer.print(" pi1:");
        pi1.print();
        Printer.print(" pi2:");
        pi2.print();
        Printer.space();
        Printer.println("table left:" + leftPaiCount);
    }

    private void printPersonView(PersonView personView, String nameSuffix){
        Printer.println(personView.getName() + nameSuffix);
        Printer.print("hand: " + personView.getHandCount());
        Printer.space();
        Printer.print("kou: ");
        personView.getEat().forEach(order->{
            for (Tile tile : order.getTiles()) {
                tile.print();
            }
            Printer.space();
        });
        Printer.space();
        Printer.print("gang:");
        for (Tile tile : personView.getKong()) {
            tile.print();
        }
        Printer.nextLine();
    }
}
