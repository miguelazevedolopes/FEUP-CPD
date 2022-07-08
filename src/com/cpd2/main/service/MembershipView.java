package com.cpd2.main.service;

public class MembershipView{
    private int membershipCount;
    private String nodeIP = new String();
    private String nodeHash = new String();;
    private int membershipPort;
    private int storagePort;
    private long lastChecked;


    public MembershipView(String nodeIP,int membershipCount,int storagePort){
        this.membershipCount=membershipCount;
        this.nodeIP=nodeIP;
        this.nodeHash = Utils.generateHash(nodeIP);
        this.storagePort=storagePort;
        this.membershipPort=storagePort+1;    
        updateLastChecked();    
    }

    public MembershipView(String str){
        String[] components = str.split(", ");
        this.nodeIP=components[0];
        this.nodeHash = Utils.generateHash(this.nodeIP);
        this.membershipCount= Integer.parseInt(components[1]);
        this.storagePort= Integer.parseInt(components[2]);
        this.membershipPort=storagePort+1;     
        this.lastChecked= Long.parseLong(components[3]);
    }

    @Override
    public String toString() {
        String retString=nodeIP+", "+String.valueOf(membershipCount)+", "+String.valueOf(storagePort)+", "+String.valueOf(lastChecked);
        return retString;
    }

    public void updateLastChecked(){
        lastChecked=System.currentTimeMillis();
    }

    public String getNodeHash() {
        return this.nodeHash;
    }

    public String getNodeIP(){
        return this.nodeIP;
    }

    public long getLastUpdated(){
        return this.lastChecked;
    }

    public int getMembershipCount(){
        return this.membershipCount;
    }

    public int getMembershipPort(){
        return this.membershipPort;
    }

    public int getStoragePort(){
        return this.storagePort;
    }


    public void increaseMembershipCount(){
        this.membershipCount++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MembershipView)) {
            return false;
        }

        MembershipView mv = (MembershipView) obj;
        return this.membershipCount==mv.getMembershipCount()&&this.nodeIP.equals(mv.getNodeIP())
        &&this.nodeHash.equals(mv.getNodeHash())&&this.membershipPort==mv.getMembershipPort()&&this.storagePort==mv.getStoragePort();
    }

}
