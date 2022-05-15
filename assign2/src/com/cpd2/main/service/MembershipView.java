package com.cpd2.main.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MembershipView {
    int membershipCount;
    int nodeID;

    public MembershipView(int nodeID,int membershipCount){
        this.membershipCount=membershipCount;
        this.nodeID=nodeID;
        saveMembershipInfo();
    }

    /**
     * Saves membership info to persistent storage
     */
    private void saveMembershipInfo(){

        // Creates directory in case it doesn't exist 
        new File("./storage/"+Integer.toString(nodeID)).mkdirs();
        
        // Creates and saves the membership info to a file
        File f= new File("./storage/"+Integer.toString(nodeID)+"/"+"membership.txt");
        try{

            f.createNewFile();
            
            FileWriter myWriter = new FileWriter(f.getAbsolutePath());

            myWriter.write(Integer.toString(this.nodeID) + "\n" + Integer.toString(this.membershipCount));
            
            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
