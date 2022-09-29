package com.catchyan.io;

import com.catchyan.entity.Pai;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Printer {
    static {
        AnsiConsole.systemInstall();
    }

    @Getter
    @AllArgsConstructor
    public enum Style {
        SIMPLE(36L, 4L, "Pai_Simple")
        ;
        private final long typeOffset;
        private final long pointOffset;
        private final String fileName;
    }

    public static void printLogo(){
        System.out.println("\n" +
                "  (`\\ .-') /` ('-. .-._   .-')             \n" +
                "   `.( OO ),'( OO )  ( '.( OO )_           \n" +
                ",--./  .--.  ,--. ,--.,--.   ,--.)    ,--. \n" +
                "|      |  |  |  | |  ||   `.'   | .-')| ,| \n" +
                "|  |   |  |, |   .|  ||         |( OO |(_| \n" +
                "|  |.'.|  |_)|       ||  |'.'|  || `-'|  | \n" +
                "|         |  |  .-.  ||  |   |  |,--. |  | \n" +
                "|   ,'.   |  |  | |  ||  |   |  ||  '-'  / \n" +
                "'--'   '--'  `--' `--'`--'   `--' `-----'  \n");
    }
    public static void print(String s){
        System.out.print(s);
    }
    public static void println(String s){
        System.out.println(s);
    }
    public static void nextLine(){
        System.out.println("");
    }
    public static void space(){
        System.out.print(" ");
    }
    public static String printAndRead(String s){
        System.out.print(s);
        return Reader.read();
    }
    public static String printlnAndRead(String s){
        System.out.println(s);
        return Reader.read();
    }

    public static void printPai(Pai pai, Style style){
        try(InputStream inputStream = Objects.requireNonNull(Printer.class.getClassLoader().getResource(style.getFileName())).openStream()){
            long pointOffset = style.getPointOffset();
            inputStream.skip(style.getTypeOffset() * pai.getType().getOffset() + (pai.getPoint() - 1) * pointOffset);
            byte[] bytes = new byte[new Long(pointOffset).intValue()];
            inputStream.read(bytes);
            String s = new String(bytes);
            System.out.print(s);
        } catch (IOException e) {
            Printer.print("exception Pai" + pai.getType() + pai.getPoint());
            throw new RuntimeException(e);
        }
    }
}
