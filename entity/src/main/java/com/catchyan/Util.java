package com.catchyan;

import cn.hutool.core.util.StrUtil;
import com.catchyan.entity.Option;
import com.catchyan.entity.Tile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    static Pattern pattern = Pattern.compile("^(chi|c|peng|p|gang|g|hu|h|pass|da|d)((\\s\\|[1-9]|\\sw[1-9]|\\s\\.[1-9]|\\s东|\\s南|\\s西|\\s北|\\s中|\\s發|\\s白){0,2})$");

    public static Option getOptionByCommand(String command){
        Matcher matcher = pattern.matcher(command);
        Option result = null;
        if(matcher.matches()){
            String typeString = matcher.group(1);
            switch (typeString) {
                case "c":
                case "chi":
                    result = new Option(Option.Type.CHOW);
                    break;
                case "peng":
                    result = new Option(Option.Type.PUNG);
                    break;
                case "g":
                case "gang":
                    result = new Option(Option.Type.GONG);
                    break;
                case "h":
                case "hu":
                    result = new Option(Option.Type.WIN);
                    break;
                case "p":
                case "pass":
                    result = new Option(Option.Type.PASS);
                    break;
                case "d":
                case "da":
                    result = new Option(Option.Type.PLAY);
                    break;
            }
            String paiString = matcher.group(2);
            if(StrUtil.isNotEmpty(paiString)){
                String[] pais = paiString.split(" ");
                for (int i = 1; i < pais.length; i++) {
                    result.getTiles().add(getPaiByCommand(pais[i]));
                }
            }
        }
        return result;
    }

    public static Tile getPaiByCommand(String command){
        Tile tile = null;
        String type = command.substring(0, 1);
        switch (type){
            case "w":
                tile = new Tile(Tile.Suit.CHARACTER);
                break;
            case "|":
                tile = new Tile(Tile.Suit.BAMBOO);
                break;
            case ".":
                tile = new Tile(Tile.Suit.DOT);
                break;
            case "东":
                tile = new Tile(Tile.Suit.WIND, 1);
                break;
            case "南":
                tile = new Tile(Tile.Suit.WIND, 2);
                break;
            case "西":
                tile = new Tile(Tile.Suit.WIND, 3);
                break;
            case "北":
                tile = new Tile(Tile.Suit.WIND, 4);
                break;
            case "中":
                tile = new Tile(Tile.Suit.WIND, 5);
                break;
            case "發":
                tile = new Tile(Tile.Suit.WIND, 6);
                break;
            case "白":
                tile = new Tile(Tile.Suit.WIND, 7);
                break;
        }
        if(tile.getPoint() == 0){
            tile.setPoint(Integer.parseInt(command.substring(1)));
        }
        return tile;
    }

}