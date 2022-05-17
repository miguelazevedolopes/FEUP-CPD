package com.cpd2.main.service;
import java.util.Map;

public class MembershipLog {
    private Map<Integer,Integer> membershipLog;

    MembershipLog(Integer nodeID, Integer membershipCount){
        membershipLog.put(nodeID, membershipCount);
    }
    
    void updateNodeView(Integer nodeID, Integer membershipCount){
        membershipLog.put(nodeID, membershipCount);
    }
}
