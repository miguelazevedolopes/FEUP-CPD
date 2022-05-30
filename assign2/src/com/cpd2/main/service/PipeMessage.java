package com.cpd2.main.service;

import java.nio.channels.Pipe;

enum PipeMessageType{
    JOIN,
    LEAVE
}

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
