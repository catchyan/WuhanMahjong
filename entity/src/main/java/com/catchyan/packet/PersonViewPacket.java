package com.catchyan.packet;

import com.catchyan.entity.Option;
import com.catchyan.entity.Pai;
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

    private List<Pai> hand = new ArrayList<>();
    private List<Pai[]> kou = new ArrayList<>();
    private List<Pai> gang = new ArrayList<>();
    private Pai lai;
    private Pai pi1;
    private Pai pi2;
    private int leftPaiCount;
    private List<Option> options = new ArrayList<>();

    @Override
    public byte getType() {
        return PERSON_VIEW;
    }
}
