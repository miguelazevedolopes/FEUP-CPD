package com.cpd2.main.service.messages;

import java.io.Serializable;

import com.cpd2.main.service.MembershipLog;
import com.cpd2.main.service.MembershipView;
import com.cpd2.main.service.messages.enums.MembershipMessageType;

public class MembershipMessage implements Serializable {
    public MembershipView mView;
    public MembershipLog mLog;
    public MembershipMessageType type;
    
    public MembershipMessage(MembershipView mv,MembershipLog ml, MembershipMessageType type){
        this.mView=mv;
        this.mLog=ml.copy();
        this.type=type;
    }
    
}
