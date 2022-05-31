package com.cpd2.main.service;

import java.io.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.cpd2.main.service.communication.UnicastService;
import com.cpd2.main.service.messages.PipeMessage;
import com.cpd2.main.service.messages.StorageMessage;
import com.cpd2.main.service.messages.enums.PipeMessageType;
import com.cpd2.main.service.messages.enums.StorageMessageType;

public class StorageService extends Thread{

    private UnicastService<StorageMessage> unicastService;
    private MembershipLog membershipLog;
    private TreeSet<String> nodeHashes;
    private MembershipView membershipView;
    private boolean stop=false;
    private LinkedList<PipeMessage> membershipStoragePipe;
    private List<String> ownedKeys= new ArrayList<>();


    public StorageService(MembershipLog membershipLog, MembershipView membershipView, TreeSet<String> nodeHashes, LinkedList<PipeMessage> membershipStoragePipe)
    {
        
        this.membershipStoragePipe=membershipStoragePipe;
        this.membershipLog=membershipLog;
        this.membershipView=membershipView;
        this.nodeHashes = nodeHashes;

        // Setting up unicast communication
        this.unicastService = new UnicastService<StorageMessage>();

        
    }

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    private MembershipView getResponsibleNodeInfo(StorageMessage message) {
        String messageHash=Utils.generateHash(message.valueToStore);
        synchronized(nodeHashes){
            String nodeHash=nodeHashes.higher(messageHash);
            if(nodeHash!=null){
                return membershipLog.getNodeInfo(nodeHash);
            }
            else{
                return membershipLog.getNodeInfo(nodeHashes.first());
            }
        }
    }

    private void saveOwnedKeyList(){
        
        // Creates and saves the membership info to a file
        File f= new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/"+membershipView.getNodeHash()+"/"+"keys");

        if(f.exists()) f.delete();

        try{

            f.createNewFile();

            FileOutputStream fStream = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fStream);
            o.writeObject(ownedKeys);
            o.close();
            fStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            
            e.printStackTrace();
        }
    }

    private String getSuccessor(String nodeHash) {
        if (nodeHashes.higher(nodeHash)==null) {
            return nodeHashes.first();
        }
        return nodeHashes.higher(nodeHash);
    }

    private void saveToFile(StorageMessage message) {
        System.out.println("Node "+ membershipView.getNodeIP() + ": Saved the file to persistent memory");
        String fileHash = Utils.generateHash(message.valueToStore);
        File f = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + membershipView.getNodeHash() +"/" + fileHash);
        try{
            System.out.println(f.getAbsolutePath());
            f.createNewFile();

            FileOutputStream fStream = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fStream);
            o.writeObject(message.valueToStore);
            o.close();
            fStream.close();
            
            ownedKeys.add(fileHash);
            saveOwnedKeyList();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleJoin(PipeMessage msg){
        if(msg.type==PipeMessageType.JOIN){
            if(getSuccessor(msg.mView.getNodeHash()).equals(membershipView.getNodeHash())){
                List<String> toRemove=new ArrayList<>();
                for (String fileKey : ownedKeys) {
                    String fileContent=getFromFile(fileKey);
                    if(getSuccessor(fileKey).equals(msg.mView.getNodeHash())){
                        unicastService.sendUnicastMessage(msg.mView.getStoragePort(), msg.mView.getNodeIP(), new StorageMessage(StorageMessageType.PUT, fileContent));
                        toRemove.add(fileKey);
                    }
                }
                for (String fileToRemove : toRemove) {
                    File f = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + membershipView.getNodeHash() + "/" + fileToRemove);
                    f.delete();
                    ownedKeys.remove(fileToRemove);
                }
            }
        }
    }

    private void leaveTransfer(){
        MembershipView transferTo=membershipLog.getNodeInfo(getSuccessor(membershipView.getNodeHash()));
        for (String fileKey : ownedKeys) {
            String fileContent=getFromFile(fileKey);
            unicastService.sendUnicastMessage(transferTo.getStoragePort(), transferTo.getNodeIP(), new StorageMessage(StorageMessageType.PUT, fileContent));
        }
        for (String fileToRemove : ownedKeys) {
            
            File f = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + membershipView.getNodeHash() + "/" + fileToRemove);
            f.delete();
            ownedKeys.remove(fileToRemove);
        }
    }

    private void replicate(MembershipView nodeInfo, StorageMessage message) {
        // String messageHash = Utils.generateHash(message.valueToStore);
        // String successor1 = getSuccessor(nodeInfo.getNodeHash());
        // if (successor1 == null) {
        //     return;
        // }
        // List<String> successors = new ArrayList<>();
        // successors.add(successor1);
        // String successor2 = getSuccessor(successor1);
        // if (!(successor2 == null) && !(successor2.equals(nodeInfo.getNodeHash()))) {
        //     successors.add(successor2);
        // }
        // for (String successor : successors) {
        //     File f = new File("./storage" + successor + "/" + messageHash);
        //     try{

        //         f.createNewFile();

        //         FileOutputStream fStream = new FileOutputStream(f);
        //         ObjectOutputStream o = new ObjectOutputStream(fStream);
        //         o.writeObject(message.valueToStore);
        //         o.close();
        //         fStream.close();

        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    private String getFromFile(String fileKey) {
        String nodeHash = membershipView.getNodeHash();
        File f = new File("/home/miguel/Documents/Faculdade/g01/assign2/storage/" + nodeHash + "/" + fileKey);
        String value = null;
        try {
            if (!f.createNewFile()) {
                FileInputStream fStream = new FileInputStream(f);
                ObjectInputStream o = new ObjectInputStream(fStream);
                value = (String) o.readObject();
                o.close();
                fStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    @Override
    public void run() {
        unicastService.startUnicastReceiver(membershipView.getStoragePort());
        nodeHashes.add(membershipView.getNodeHash());
        while(keepRunning()){
            if(unicastService.getNumberOfObjectsReceived()>0){

                StorageMessage message = unicastService.getLastObjectReceived();
                MembershipView nodeInfo= getResponsibleNodeInfo(message);

                switch (message.type){
                    case PUT:
                        if(nodeInfo.getNodeIP().equals(membershipView.getNodeIP())){
                            saveToFile(message);
                            if(membershipLog.getActiveNodeCount()>3){
                                //replicate(nodeInfo, message);
                            }
                        }
                        else{
                            unicastService.sendUnicastMessage(nodeInfo.getStoragePort(), nodeInfo.getNodeIP(), message);
                        }
                        break;
                    case PUT_REPLICATE:
                        saveToFile(message);
                        break;
                    case GET:
                        String fKey=Utils.generateHash(message.valueToStore);
                        if(ownedKeys.contains(fKey)){
                            String fileContents = getFromFile(fKey);
                            // send file contents back to caller
                        }
                        break;
                    case DELETE:
                        // deleteFile();
                        // replicate();
                        break;
                    default:
                        break;
                }
            
            }
            if(!membershipStoragePipe.isEmpty()){
                System.out.println("Node "+ membershipView.getNodeIP() + ": Recebeu JOIN");
                handleJoin(membershipStoragePipe.poll());
                /** Ao fazer join adiciona hash ao tree set e redistribui keys, se existirem. Same para o leave. */
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        leaveTransfer();
    }
    
}

    
