package com.cpd2.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MembershipView{
    private int membershipCount;
    private String nodeIP = new String();
    private String nodeHash = new String();;
    private int membershipPort;
    private int storagePort;

    public MembershipView(String nodeIP,int membershipCount,int storagePort){
        this.membershipCount=membershipCount;
        this.nodeIP=nodeIP;
        this.nodeHash = Utils.generateHash(nodeIP);
        this.storagePort=storagePort;
        this.membershipPort=storagePort+1;        
    }

    public MembershipView(String str){
        String[] components = str.split(", ");
        this.nodeIP=components[0];
        this.nodeHash = Utils.generateHash(this.nodeIP);
        this.membershipCount= Integer.parseInt(components[1]);
        this.storagePort= Integer.parseInt(components[2]);
        this.membershipPort=storagePort+1;     
    }

    @Override
    public String toString() {
        String retString=nodeIP+", "+String.valueOf(membershipCount)+", "+String.valueOf(storagePort);
        return retString;
    }

    public String getNodeHash() {
        return this.nodeHash;
    }

    public String getNodeIP(){
        return this.nodeIP;
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

    
    
    /**
     * Saves membership info to persistent storage
     */
    public void saveMembershipInfo(){

        // Creates directory in case it doesn't exist 
        var temp=new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/"+nodeHash);
        temp.mkdirs();
        System.out.println(temp.getAbsolutePath());
        
        // Creates and saves the membership info to a file
        File f= new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/"+nodeHash+"/"+"membership");
        try{

            f.createNewFile();

            // FileOutputStream fStream = new FileOutputStream(f);
            // ObjectOutputStream o = new ObjectOutputStream(fStream);
            // o.writeObject(this);
            // o.close();
            // fStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            
            e.printStackTrace();
        }
        
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
