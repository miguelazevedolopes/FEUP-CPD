package com.cpd2.main.service;

import com.cpd2.main.Pair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MembershipLog implements Serializable{
    //private Map<Integer,Integer> membershipLog=new HashMap<>();
    private List<Pair<Integer, Integer>> memLog = new ArrayList<>();
    

    MembershipLog(int nodeID, int membershipCount){
        //membershipLog.put(nodeID, membershipCount);
        memLog.add(new Pair<>(nodeID, membershipCount));
    }


    /**
     * Updates the log table, maintaining consistency in its logs
     * @param nodeID the node to be updated
     * @param membershipCount the new count to be set
     */
    void updateNodeView(int nodeID, int membershipCount){

        Pair<Integer, Integer> toBeAdded = new Pair<>(nodeID, membershipCount);

        for(int i = 0; i < memLog.size(); i++)
        {
            //Event is already in the local log
            if(memLog.get(i).equals(toBeAdded))
                return;
            //The event is older than the event for that member in the local log
            else if(memLog.get(i).getL().equals(toBeAdded.getL()) && memLog.get(i).getR() > membershipCount)
                return;
            //Event is newer than the event for that member in the local log
            else if(memLog.get(i).getL().equals(toBeAdded.getL()) && memLog.get(i).getR() < membershipCount)
            {
                memLog.remove(memLog.get(i));
                memLog.add(toBeAdded);
                return;
            }
        }
        //New event
        memLog.add(toBeAdded);
    }

    void checkUpdated(MembershipLog toBeCompared)
    {
        for(int i = 0; i < toBeCompared.memLog.size(); i++)
        {
            updateNodeView(toBeCompared.memLog.get(i).getL(), toBeCompared.memLog.get(i).getR());
        }
    }

    public Boolean has(int nodeID){
        for (Pair<Integer,Integer> pair : memLog) {
            if(pair.getL()==nodeID){
                return true;
            }
        }
        return false;
    }

    public int getLogSize(){
        return memLog.size();
    }

    public MembershipLog copy(){
        MembershipLog copyObject= new MembershipLog(0, 0);
        copyObject.memLog=this.memLog;
        return copyObject;
    }

    public int getMembershipCount(int nodeID){
        for (Pair<Integer,Integer> pair : memLog) {
            if(pair.getL().equals(nodeID)){
                return pair.getR();
            }
        }
        return -1;
    }


    @Override
    public String toString() {
        String retString="";
        for (Pair<Integer,Integer> pair : memLog) {
            retString+= "Node ID: "+ pair.getL()+", Membership Count: "+pair.getR()+"\n";
        }
        return retString;
    }
}
