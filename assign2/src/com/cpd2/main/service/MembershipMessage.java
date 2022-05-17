package com.cpd2.main.service;

import java.io.Serializable;

public class MembershipMessage implements Serializable {
    MembershipView mView;
    MembershipLog mLog;
    
    public MembershipMessage(MembershipView mv,MembershipLog ml){
        this.mView=mv;
        this.mLog=ml;
    }
    
}
