package com.cpd2.main.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MembershipLog implements Serializable{
    //private Map<Integer,Integer> membershipLog=new HashMap<>();
    private List<MembershipView> memLog = new ArrayList<>();
    

    MembershipLog(MembershipView mv){
        //membershipLog.put(nodeID, membershipCount);
        memLog.add(mv);
    }


    /**
     * Updates the log table, maintaining consistency in its logs
     * @param nodeID the node to be updated
     * @param membershipCount the new count to be set
     */
    void updateNodeView(MembershipView mv){
        synchronized(memLog){
            for(int i = 0; i < memLog.size(); i++)
            {
                //Event is already in the local log
                if(memLog.get(i).equals(mv))
                    return;
                //The event is older than the event for that member in the local log
                else if(memLog.get(i).getNodeIP().equals(mv.getNodeIP()) && memLog.get(i).getMembershipCount() > mv.getMembershipCount())
                    return;
                //Event is newer than the event for that member in the local log
                else if(memLog.get(i).getNodeIP().equals(mv.getNodeIP()) && memLog.get(i).getMembershipCount() < mv.getMembershipCount())
                {
                    memLog.remove(memLog.get(i));
                    memLog.add(mv);
                    return;
                }
            }
            //New event
            memLog.add(mv);
        }
    }

    void checkUpdated(MembershipLog toBeCompared)
    {
        for(int i = 0; i < toBeCompared.memLog.size(); i++)
        {
            updateNodeView(toBeCompared.memLog.get(i));
        }
    }

    public Boolean has(String nodeID){
        for (MembershipView mv : memLog) {
            if(mv.getNodeIP().equals(nodeID)){
                return true;
            }
        }
        return false;
    }

    public int getLogSize(){
        return memLog.size();
    }

    public MembershipLog copy(){
        MembershipLog copyObject= new MembershipLog(new MembershipView("", 0, 0));
        copyObject.memLog=this.memLog;
        return copyObject;
    }

    public int getMembershipCount(String nodeIP){
        for (MembershipView mv : memLog) {
            if(mv.getNodeIP().equals(nodeIP)){
                return mv.getMembershipCount();
            }
        }
        return -1;
    }

    public MembershipView getNodeInfo(String nodeHash){
        for (MembershipView membershipView : memLog) {
            if(membershipView.getNodeHash().equals(nodeHash)) return membershipView;
        }
        return null;
    }

    @Override
    public String toString() {
        String retString="";
        for (MembershipView mv : memLog) {
            retString+= "Node ID: "+ mv.getNodeIP()+", Membership Count: "+mv.getMembershipCount()+"\n";
        }
        return retString;
    }
}
