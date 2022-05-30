package com.cpd2.main.service;

import java.io.Serializable;

enum MembershipMessageType{
    JOIN,
    JOIN_RESPONSE,
    PERIODIC,
    LEAVE
}

public class MembershipMessage implements Serializable {
    MembershipView mView;
    MembershipLog mLog;
    MembershipMessageType type;
    
    public MembershipMessage(MembershipView mv,MembershipLog ml, MembershipMessageType type){
        this.mView=mv;
        this.mLog=ml.copy();
        this.type=type;
    }
    
}
