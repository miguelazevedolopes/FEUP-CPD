package com.cpd2.main.service;

import java.io.Serializable;

enum MessageType{
    JOIN,
    JOIN_RESPONSE,
    PERIODIC,
    LEAVE
}

public class MembershipMessage implements Serializable {
    MembershipView mView;
    MembershipLog mLog;
    MessageType type;
    
    public MembershipMessage(MembershipView mv,MembershipLog ml, MessageType type){
        this.mView=mv;
        this.mLog=ml.copy();
        this.type=type;
    }
    
}
