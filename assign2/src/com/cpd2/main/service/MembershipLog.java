package com.cpd2.main.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MembershipLog implements Serializable{
    //private Map<Integer,Integer> membershipLog=new HashMap<>();
    private List<MembershipView> memLog = new ArrayList<>();
    private boolean upToDate=false;

    MembershipLog(MembershipView mv){
        //membershipLog.put(nodeID, membershipCount);
        memLog.add(mv);
    }

    public boolean isUpToDate(){
        return upToDate;
    }

    /**
     * Updates the log table, maintaining consistency in its logs
     * @param nodeID the node to be updated
     * @param membershipCount the new count to be set
     */
    void updateNodeView(MembershipView mv, TreeSet<String> nodeHashes){
        synchronized(memLog){
            for(int i = 0; i < memLog.size(); i++)
            {
                //Event is already in the local log
                if(memLog.get(i).equals(mv))
                {
                    upToDate=true;
                    return;
                }
                //The event is older than the event for that member in the local log
                else if(memLog.get(i).getNodeIP().equals(mv.getNodeIP()) && memLog.get(i).getMembershipCount() > mv.getMembershipCount())
                {
                    upToDate=true;
                    return;
                }
                //Event is newer than the event for that member in the local log
                else if(memLog.get(i).getNodeIP().equals(mv.getNodeIP()) && memLog.get(i).getMembershipCount() < mv.getMembershipCount())
                {
                    upToDate=false;
                    memLog.remove(memLog.get(i));
                    memLog.add(mv);
                    if(mv.getMembershipCount()%2!=0){
                        synchronized(nodeHashes){
                            if(nodeHashes.contains(mv.getNodeHash())){
                                nodeHashes.remove(mv.getNodeHash());
                            }
                        }
                    }
                    return;
                }
            }
            //New event
            synchronized(nodeHashes){
                if(!nodeHashes.contains(mv.getNodeHash())){
                    nodeHashes.add(mv.getNodeHash());
                }
            }
            memLog.add(mv);
        }
    }

    void checkUpdated(MembershipLog toBeCompared, TreeSet<String> nodeHashes)
    {
        for(int i = 0; i < toBeCompared.memLog.size(); i++)
        {
            updateNodeView(toBeCompared.memLog.get(i), nodeHashes);
        }
    }

    public boolean has(String nodeID){
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

    public int getActiveNodeCount(){
        int counter=0;
        for (MembershipView membershipView : memLog) {
            if(membershipView.getMembershipCount()%2==0){
                counter++;
            }
        }
        return counter;
    }
}
