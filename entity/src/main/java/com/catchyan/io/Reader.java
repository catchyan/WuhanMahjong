package com.catchyan.io;

import com.catchyan.entity.Option;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String read(){
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Option readCommand(){
        return null;
    }
    public static int readInt() {
        try{
            return Integer.parseInt(br.readLine());
        } catch (Exception e) {
            Printer.println("invalid int input, try again");
            return readInt();
        }
    }
}
