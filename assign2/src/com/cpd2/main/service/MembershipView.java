package com.cpd2.main.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MembershipView implements Serializable{
    int membershipCount;
    String nodeIP = new String();
    String nodeHash = new String();;
    int membershipPort;
    int storagePort;

    public MembershipView(String nodeID,int membershipCount,int storagePort){
        this.membershipCount=membershipCount;
        this.nodeIP=nodeID;
        this.nodeHash = generateHash();
        this.storagePort=storagePort;
        this.membershipPort=storagePort+1;
        saveMembershipInfo();
    }

    public MembershipView(String pathToFile){
        try {
            FileInputStream fStream = new FileInputStream(new File(pathToFile));
            ObjectInputStream o = new ObjectInputStream(fStream);
            MembershipView object = (MembershipView) o.readObject();
            this.membershipCount=object.membershipCount;
            this.nodeIP=object.nodeIP;
            this.nodeHash=object.nodeHash;
            this.membershipPort=object.membershipPort;
            o.close();
            fStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
     * Generates hash for the node id
     * @return Hash
     * @throws NoSuchAlgorithmException
     */
    private String generateHash() {
        MessageDigest md=null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] hash = md.digest(nodeIP.getBytes(StandardCharsets.UTF_8));
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }
    
    /**
     * Saves membership info to persistent storage
     */
    private void saveMembershipInfo(){

        // Creates directory in case it doesn't exist 
        new File("./storage/"+nodeHash).mkdirs();
        
        // Creates and saves the membership info to a file
        File f= new File("./storage/"+nodeHash+"/"+"membership");
        try{

            f.createNewFile();

            FileOutputStream fStream = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fStream);
            o.writeObject(this);
            o.close();
            fStream.close();

        } catch (Exception e) {
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
