package com.cpd2.main.service;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.cpd2.main.service.messages.MembershipMessage;

public class Utils {
    /**
     * Generates hash for the node id
     * @return Hash
     * @throws NoSuchAlgorithmException
     */
    public static String generateHash(String valToHash) {
        MessageDigest md=null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] hash = md.digest(valToHash.getBytes(StandardCharsets.UTF_8));
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
    public static void saveMembershipInfo(MembershipMessage membershipMessage){

        // Creates directory in case it doesn't exist 
        var temp=new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/"+membershipMessage.mView.getNodeHash());
        temp.mkdirs();
        System.out.println(temp.getAbsolutePath());
        
        // Creates and saves the membership info to a file
        File f= new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/"+membershipMessage.mView.getNodeHash()+"/"+"membership.txt");
        try{
            if(f.exists()) f.delete();
            f.createNewFile();
            FileWriter myWriter = new FileWriter(f.getAbsolutePath());
            myWriter.write(membershipMessage.toString());
            myWriter.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
}
