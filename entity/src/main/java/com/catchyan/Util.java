package com.catchyan;

import cn.hutool.core.util.StrUtil;
import com.catchyan.entity.Option;
import com.catchyan.entity.Pai;

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
                    result = new Option(Option.Type.CHI);
                    break;
                case "peng":
                    result = new Option(Option.Type.PENG);
                    break;
                case "g":
                case "gang":
                    result = new Option(Option.Type.GANG);
                    break;
                case "h":
                case "hu":
                    result = new Option(Option.Type.HU);
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
                    result.getPais().add(getPaiByCommand(pais[i]));
                }
            }
        }
        return result;
    }

    public static Pai getPaiByCommand(String command){
        Pai pai = null;
        String type = command.substring(0, 1);
        switch (type){
            case "w":
                pai = new Pai(Pai.Type.WAN);
                break;
            case "|":
                pai = new Pai(Pai.Type.TIAO);
                break;
            case ".":
                pai = new Pai(Pai.Type.TONG);
                break;
            case "东":
                pai = new Pai(Pai.Type.FENG, 1);
                break;
            case "南":
                pai = new Pai(Pai.Type.FENG, 2);
                break;
            case "西":
                pai = new Pai(Pai.Type.FENG, 3);
                break;
            case "北":
                pai = new Pai(Pai.Type.FENG, 4);
                break;
            case "中":
                pai = new Pai(Pai.Type.FENG, 5);
                break;
            case "發":
                pai = new Pai(Pai.Type.FENG, 6);
                break;
            case "白":
                pai = new Pai(Pai.Type.FENG, 7);
                break;
        }
        if(pai.getPoint() == 0){
            pai.setPoint(Integer.parseInt(command.substring(1)));
        }
        return pai;
    }

}