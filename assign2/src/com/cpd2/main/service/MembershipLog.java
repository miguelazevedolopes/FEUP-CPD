package com.cpd2.main.service;

import com.cpd2.main.Pair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembershipLog implements Serializable{
    //private Map<Integer,Integer> membershipLog=new HashMap<>();
    private List<Pair<Integer, Integer>> memLog = new ArrayList<>();

    MembershipLog(Integer nodeID, Integer membershipCount){
        //membershipLog.put(nodeID, membershipCount);
        memLog.add(new Pair<>(nodeID, membershipCount));
    }


    /**
     * Updates the log table, maintaining consistency in its logs
     * @param nodeID the node to be updated
     * @param membershipCount the new count to be set
     */
    void updateNodeView(Integer nodeID, Integer membershipCount){

        Pair<Integer, Integer> toBeAdded = new Pair<>(nodeID, membershipCount);

        for(int i = 0; i < memLog.size(); i++)
        {
            //Event is already in the local log
            if(memLog.get(i).isEqual(toBeAdded))
                return;
            //The event is older than the event for that member in the local log
            else if(memLog.get(i).getL() == toBeAdded.getL() && memLog.get(i).getR() > membershipCount)
                return;
            //Event is newer than the event for that member in the local log
            else if(memLog.get(i).getL() == toBeAdded.getL() && memLog.get(i).getR() < membershipCount)
            {
                memLog.remove(memLog.get(i));
                memLog.add(toBeAdded);
                return;
            }
        }
        //New event
        memLog.add(toBeAdded);
    }

    void checkUpdated(List<Pair<Integer, Integer>> toBeCompared)
    {
        for(int i = 0; i < toBeCompared.size(); i++)
        {
            updateNodeView(toBeCompared.get(i).getL(), toBeCompared.get(i).getR());
        }
    }
}
