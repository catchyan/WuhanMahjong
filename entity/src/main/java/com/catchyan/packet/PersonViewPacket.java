package com.catchyan.packet;

import com.catchyan.entity.Option;
import com.catchyan.entity.Order;
import com.catchyan.entity.Tile;
import com.catchyan.entity.PersonView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.catchyan.packet.PacketType.PERSON_VIEW;

@Getter
@Setter
public class PersonViewPacket extends BasePacket{
    private PersonView prev;
    private PersonView opposite;
    private PersonView next;

    private List<Tile> hand = new ArrayList<>();
    private List<Order> eat = new ArrayList<>();
    private List<Tile> gang = new ArrayList<>();
    private Tile rascal;
    private Tile kong1;
    private Tile kong2;
    private int leftPaiCount;
    private List<Option> options = new ArrayList<>();

    @Override
    public byte getType() {
        return PERSON_VIEW;
    }
}
