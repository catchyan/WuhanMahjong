package com.catchyan.handler;

import com.catchyan.Util;
import com.catchyan.entity.Option;
import com.catchyan.entity.Pai;
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
        printTable(packet.getLai(), packet.getPi1(), packet.getPi2(), packet.getLeftPaiCount());
        printSelfInfo(packet.getHand(), packet.getKou(), packet.getGang(), packet.getOptions());

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

    private void printSelfInfo(List<Pai> hand, List<Pai[]> kou, List<Pai> gang, List<Option> options) {
        Printer.print("kou: ");
        for (Pai[] pais : kou) {
            for (Pai pai : pais) {
                pai.print();
            }
            Printer.space();
        }
        Printer.print(" gang: ");
        Printer.nextLine();
        for (Pai pai : gang) {
            pai.print();
            Printer.space();
        }
        Printer.nextLine();
        Printer.print(" hand: ");
        for (Pai pai : hand) {
            pai.print();
        }
        if(options.size() > 0){
            Printer.print(" operation: ");
        }
        for (Option option : options) {
            option.print();
        }
    }

    private void printTable(Pai lai, Pai pi1, Pai pi2, int leftPaiCount) {
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
        personView.getKou().forEach(pais->{
            for (Pai pai : pais) {
                pai.print();
            }
            Printer.space();
        });
        Printer.space();
        Printer.print("gang:");
        for (Pai pai : personView.getGang()) {
            pai.print();
        }
        Printer.nextLine();
    }
}
