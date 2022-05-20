package com.cpd2.main.service;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MembershipLog implements Serializable{
    private Map<Integer,Integer> membershipLog=new HashMap<>();

    MembershipLog(Integer nodeID, Integer membershipCount){
        membershipLog.put(nodeID, membershipCount);
    }


    /**
     * Updates the log table, maintaining consistency in its logs
     * @param nodeID the node to be updated
     * @param membershipCount the new count to be set
     */
    void updateNodeView(Integer nodeID, Integer membershipCount){

        if(membershipLog.containsKey(nodeID))
        {
            //Event is already in the local log or if the event is older than the event for that member in the local log
            if(membershipLog.get(nodeID) == membershipCount || membershipLog.get(nodeID) > membershipCount)
                return;
            //Event is newer than the event for that member in the local log
            else
            {
                membershipLog.remove(nodeID, membershipLog.get(nodeID));
                membershipLog.put(nodeID, membershipCount);
            }
        }
        else //New event
        {
            membershipLog.put(nodeID, membershipCount);
        }
    }
}
