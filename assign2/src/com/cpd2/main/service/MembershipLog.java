package com.cpd2.main.service;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MembershipLog implements Serializable{
    private Map<Integer,Integer> membershipLog=new HashMap<>();

    MembershipLog(Integer nodeID, Integer membershipCount){
        membershipLog.put(nodeID, membershipCount);
    }
    
    void updateNodeView(Integer nodeID, Integer membershipCount){
        membershipLog.put(nodeID, membershipCount);
    }
}
