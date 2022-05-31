package com.cpd2.main.service.messages;

import com.cpd2.main.service.MembershipView;
import com.cpd2.main.service.messages.enums.PipeMessageType;

public class PipeMessage {
    public MembershipView mView;
    public PipeMessageType type;

    public PipeMessage(PipeMessageType type, MembershipView mView){
        this.type=type;
        this.mView=mView;
    }

    public PipeMessage(PipeMessageType type){
        this.type=type;
    }
}
