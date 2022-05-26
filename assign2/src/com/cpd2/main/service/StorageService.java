package com.cpd2.main.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class StorageService extends Thread{

    private UnicastService<StorageMessage> unicastService;
    private MembershipLog membershipLog;
    private TreeSet<String> nodeHashes;
    private MembershipView membershipView;
    private boolean stop=false;

    public StorageService(MembershipLog membershipLog, MembershipView membershipView){
        this.nodeHashes = new TreeSet<>();

        // Setting up unicast communication
        this.unicastService = new UnicastService<StorageMessage>();
        this.membershipLog=membershipLog;
        this.membershipView=membershipView;
    }

    private void addNodeHash(String nodeHash) {
        nodeHashes.add(nodeHash);
    }

    private void removeNodeHash(String nodeHash) {
        nodeHashes.remove(nodeHash);
    }

    private synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    private MembershipView getResponsibleNodeInfo(StorageMessage message) {
        String messageHash=Utils.generateHash(message.valueToStore);
        String nodeHash=nodeHashes.higher(messageHash);
        if(nodeHash!=null){
            return membershipLog.getNodeInfo(nodeHash);
        }
        else{
            return membershipLog.getNodeInfo(nodeHashes.first());
        }
    }

    private String getSuccessor(String nodeHash) {
        if (nodeHashes.size() < 2) {
            return null;
        }
        if (nodeHashes.last().equals(nodeHash)) {
            return nodeHashes.first();
        }
        return nodeHashes.higher(nodeHash);
    }

    private void saveToFile(MembershipView nodeInfo, StorageMessage message) {
        String messageHash = Utils.generateHash(message.valueToStore);
        File f = new File("./storage/" + nodeInfo.getNodeHash() +"/" + messageHash);
        try{

            f.createNewFile();

            FileOutputStream fStream = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fStream);
            o.writeObject(message.valueToStore);
            o.close();
            fStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replicate(MembershipView nodeInfo, StorageMessage message) {
        String messageHash = Utils.generateHash(message.valueToStore);
        String successor1 = getSuccessor(nodeInfo.getNodeHash());
        if (successor1 == null) {
            return;
        }
        List<String> successors = new ArrayList<>();
        successors.add(successor1);
        String successor2 = getSuccessor(successor1);
        if (!(successor2 == null) && !(successor2.equals(nodeInfo.getNodeHash()))) {
            successors.add(successor2);
        }
        for (String successor : successors) {
            File f = new File("./storage" + successor + "/" + messageHash);
            try{

                f.createNewFile();

                FileOutputStream fStream = new FileOutputStream(f);
                ObjectOutputStream o = new ObjectOutputStream(fStream);
                o.writeObject(message.valueToStore);
                o.close();
                fStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getFromFile(MembershipView nodeInfo, StorageMessage message) {
        String nodeHash = nodeInfo.getNodeHash();
        File f = new File("./storage" + nodeHash + "/" + message.valueToStore);
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

        while(keepRunning()){
            if(unicastService.getNumberOfObjectsReceived()>0){
                StorageMessage message = unicastService.getLastObjectReceived();

                MembershipView nodeInfo= getResponsibleNodeInfo(message);

                if(nodeInfo.getNodeIP().equals(membershipView.getNodeIP())){
                    switch (message.type){
                        case PUT:
                            saveToFile(nodeInfo, message);
                            replicate(nodeInfo, message);
                            break;
                        case GET:
                            // getFromFile();
                            break;
                        case DELETE:
                            // deleteFile();
                            // replicate();
                            break;
                        default:
                            break;
                    }
                    
                }
                else{
                    unicastService.sendUnicastMessage(nodeInfo.getStoragePort(), nodeInfo.getNodeIP(), message);
                }
            }
            else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
}

    
