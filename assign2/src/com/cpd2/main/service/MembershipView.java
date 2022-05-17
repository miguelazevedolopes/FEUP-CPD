package com.cpd2.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MembershipView implements Serializable{
    int membershipCount;
    int nodeID;

    public MembershipView(int nodeID,int membershipCount){
        this.membershipCount=membershipCount;
        this.nodeID=nodeID;
        saveMembershipInfo();
    }

    public MembershipView(String pathToFile){
        try {
            FileInputStream fStream = new FileInputStream(new File(pathToFile));
            ObjectInputStream o = new ObjectInputStream(fStream);
            MembershipView object = (MembershipView) o.readObject();
            this.membershipCount=object.membershipCount;
            this.nodeID=object.nodeID;
            o.close();
            fStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Saves membership info to persistent storage
     */
    private void saveMembershipInfo(){

        // Creates directory in case it doesn't exist 
        new File("./storage/"+Integer.toString(nodeID)).mkdirs();
        
        // Creates and saves the membership info to a file
        File f= new File("./storage/"+Integer.toString(nodeID)+"/"+"membership");
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

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "membershipCount:"+this.membershipCount+"\nnodeID="+this.nodeID;
    }




}
