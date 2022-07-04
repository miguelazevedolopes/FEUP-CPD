package com.cpd2.main.service;

import java.io.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.cpd2.main.communication.UnicastService;
import com.cpd2.main.messages.PipeMessage;
import com.cpd2.main.messages.StorageMessage;
import com.cpd2.main.messages.enums.PipeMessageType;
import com.cpd2.main.messages.enums.StorageMessageType;
import com.cpd2.main.rmi.KeyValueStore;

public class StorageService extends Thread{

    private UnicastService unicastService;
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
        this.unicastService = new UnicastService();

    }

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    private MembershipView getResponsibleNodeInfo(StorageMessage message) {
        String messageHash=Utils.generateHash(message.contents);
        synchronized(nodeHashes){
            String nodeHash=nodeHashes.higher(messageHash);
            if(nodeHash!=null){
                System.out.println("Responsible node is Node "+ membershipLog.getNodeInfo(nodeHash).getNodeIP());
                return membershipLog.getNodeInfo(nodeHash);
            }
            else{
                System.out.println("Responsible node is Node "+ membershipLog.getNodeInfo(nodeHashes.first()).getNodeIP());
                return membershipLog.getNodeInfo(nodeHashes.first());
            }
        }
    }

    private void saveOwnedKeyList(){
        
        // Creates and saves the membership info to a file
        File f= new File(Utils.getRelativePath()+membershipView.getNodeHash()+"/"+"keys.txt");

        if(f.exists()) f.delete();

        try{

            f.createNewFile();
            FileWriter myWriter = new FileWriter(f.getAbsolutePath());
            myWriter.write(ownedKeys.toString());
            myWriter.close();

        } catch (Exception e) {
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
        String fileHash = Utils.generateHash(message.contents);
        File dir = new File(Utils.getRelativePath() + membershipView.getNodeHash());
        if(!dir.exists()) dir.mkdir();
        File f = new File(Utils.getRelativePath() + membershipView.getNodeHash() +"/" + fileHash);
        try{
            f.createNewFile();

            FileOutputStream fStream = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fStream);
            o.writeObject(message.contents);
            o.close();
            fStream.close();
            
            if(!ownedKeys.contains(fileHash)){
                ownedKeys.add(fileHash);
                saveOwnedKeyList();
            }
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
                        unicastService.sendUnicastMessage(msg.mView.getStoragePort(), msg.mView.getNodeIP(), new StorageMessage(StorageMessageType.PUT, fileContent).toString());
                        toRemove.add(fileKey);
                    }
                }
                for (String fileToRemove : toRemove) {
                    File f = new File(Utils.getRelativePath() + membershipView.getNodeHash() + "/" + fileToRemove);
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
            unicastService.sendUnicastMessage(transferTo.getStoragePort(), transferTo.getNodeIP(), new StorageMessage(StorageMessageType.PUT, fileContent).toString());
        }
        File f = new File(Utils.getRelativePath() + membershipView.getNodeHash());
        for(String s: f.list()){
            if(s.equals("membership.txt")) continue;
            File currentFile = new File(f.getPath(),s);
            currentFile.delete();
        }
    }


    private String getFromFile(String fileKey) {
        String nodeHash = membershipView.getNodeHash();
        File f = new File(Utils.getRelativePath() + nodeHash + "/" + fileKey);
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

    private void deleteFile(String fileKey){
        String nodeHash = membershipView.getNodeHash();
        File f = new File(Utils.getRelativePath() + nodeHash + "/" + fileKey);
        if(f.exists()){
            f.delete();
        }
        if(ownedKeys.contains(fileKey)){
            ownedKeys.remove(fileKey);
        }
    }

    public synchronized void put(StorageMessage message){
        MembershipView nodeInfo= getResponsibleNodeInfo(message);
        if(nodeInfo.getNodeIP().equals(membershipView.getNodeIP())){
            saveToFile(message);
 
            if(nodeHashes.size()>=3){
                String successorOneHash = getSuccessor(membershipView.getNodeHash());
                String successorTwoHash = getSuccessor(successorOneHash);
    
                MembershipView succesorOneInfo= membershipLog.getNodeInfo(successorOneHash);
                MembershipView succesorTwoInfo= membershipLog.getNodeInfo(successorTwoHash);

                unicastService.sendUnicastMessage(succesorOneInfo.getStoragePort(), succesorOneInfo.getNodeIP(), new StorageMessage(StorageMessageType.PUT_REPLICATE, message.contents).toString());
                unicastService.sendUnicastMessage(succesorTwoInfo.getStoragePort(), succesorTwoInfo.getNodeIP(), new StorageMessage(StorageMessageType.PUT_REPLICATE, message.contents).toString());
            }
            else if(nodeHashes.size()==2){
                String successorOneHash = getSuccessor(membershipView.getNodeHash());
                MembershipView succesorOneInfo= membershipLog.getNodeInfo(successorOneHash);
                unicastService.sendUnicastMessage(succesorOneInfo.getStoragePort(), succesorOneInfo.getNodeIP(), new StorageMessage(StorageMessageType.PUT_REPLICATE, message.contents).toString());
            }
            
        }
        else{
            unicastService.sendUnicastMessage(nodeInfo.getStoragePort(), nodeInfo.getNodeIP(), message.toString());
        }
    }

    public synchronized void delete(StorageMessage message){
        MembershipView nodeInfo= getResponsibleNodeInfo(message);
        if(nodeInfo.getNodeIP().equals(membershipView.getNodeIP())){
            if(ownedKeys.contains(message.contents)){
                deleteFile(message.contents);
                saveOwnedKeyList();
            }
            if(nodeHashes.size()>=3){
                String successorOneHash = getSuccessor(membershipView.getNodeHash());
                String successorTwoHash = getSuccessor(successorOneHash);
    
                MembershipView succesorOneInfo= membershipLog.getNodeInfo(successorOneHash);
                MembershipView succesorTwoInfo= membershipLog.getNodeInfo(successorTwoHash);

                unicastService.sendUnicastMessage(succesorOneInfo.getStoragePort(), succesorOneInfo.getNodeIP(), new StorageMessage(StorageMessageType.DELETE_REPLICATE, message.contents).toString());
                unicastService.sendUnicastMessage(succesorTwoInfo.getStoragePort(), succesorTwoInfo.getNodeIP(), new StorageMessage(StorageMessageType.DELETE_REPLICATE, message.contents).toString());
            }
            else if(nodeHashes.size()==2){
                String successorOneHash = getSuccessor(membershipView.getNodeHash());
                MembershipView succesorOneInfo= membershipLog.getNodeInfo(successorOneHash);
                unicastService.sendUnicastMessage(succesorOneInfo.getStoragePort(), succesorOneInfo.getNodeIP(), new StorageMessage(StorageMessageType.DELETE_REPLICATE, message.contents).toString());
            }

        }
        else{
            unicastService.sendUnicastMessage(nodeInfo.getStoragePort(), nodeInfo.getNodeIP(), message.toString());
        }
        // replicate();
    }

    public synchronized String get(StorageMessage message){
        MembershipView nodeInfo= getResponsibleNodeInfo(message);
        if(ownedKeys.contains(message.contents)){ // If it has the file, it returns it
            return getFromFile(message.contents);
        }
        else if(nodeInfo.getNodeIP().equals(membershipView.getNodeIP())){ // It's the responsible node, but for some reason it doesn't have the file

            String file=null;
            //Try first replica
            nodeInfo=membershipLog.getNodeInfo(getSuccessor(nodeInfo.getNodeHash()));
            try {
                KeyValueStore responsibleNode = (KeyValueStore)Naming.lookup("rmi://"+nodeInfo.getNodeIP()+"/"+nodeInfo.getNodeHash());
                file = responsibleNode.get(message.contents);
                return file;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //Try second replica
            nodeInfo=membershipLog.getNodeInfo(getSuccessor(nodeInfo.getNodeHash()));
            try {
                KeyValueStore responsibleNode = (KeyValueStore)Naming.lookup("rmi://"+nodeInfo.getNodeIP()+"/"+nodeInfo.getNodeHash());
                file = responsibleNode.get(message.contents);
                return file;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{ // It's not the responsible node, so it invokes the remote method in the responsible node, and then the replicas, if necessary

            String file=null;
            // Try responsible
            try {
                KeyValueStore responsibleNode = (KeyValueStore)Naming.lookup("rmi://"+nodeInfo.getNodeIP()+"/"+nodeInfo.getNodeHash());
                file = responsibleNode.get(message.contents);
                return file;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //Try first replica
            nodeInfo=membershipLog.getNodeInfo(getSuccessor(nodeInfo.getNodeHash()));
            try {
                KeyValueStore responsibleNode = (KeyValueStore)Naming.lookup("rmi://"+nodeInfo.getNodeIP()+"/"+nodeInfo.getNodeHash());
                file = responsibleNode.get(message.contents);
                return file;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //Try second replica
            nodeInfo=membershipLog.getNodeInfo(getSuccessor(nodeInfo.getNodeHash()));
            try {
                KeyValueStore responsibleNode = (KeyValueStore)Naming.lookup("rmi://"+nodeInfo.getNodeIP()+"/"+nodeInfo.getNodeHash());
                file = responsibleNode.get(message.contents);
                return file;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public void run() {
        unicastService.startUnicastReceiver(membershipView.getStoragePort());
        System.out.println("Starting unicast receiver on port: "+ membershipView.getStoragePort());
        nodeHashes.add(membershipView.getNodeHash());
        while(keepRunning()){
            if(unicastService.getNumberOfObjectsReceived()>0){
                System.out.println("Recebu msg");
                StorageMessage message = new StorageMessage(unicastService.getLastObjectReceived());
                System.out.println("Message type:" + message.type.toString());
                switch (message.type){
                    case PUT:
                        System.out.println("Node "+ membershipView.getNodeIP() + ": Received a PUT request");
                        put(message);
                        break;
                    case PUT_REPLICATE:
                        System.out.println("Node "+ membershipView.getNodeIP() + ": Received a PUT_REPLICATE request");
                        saveToFile(message);
                        break;
                    case DELETE:
                        System.out.println("Node "+ membershipView.getNodeIP() + ": Received a DELETE request");
                        delete(message);
                        break;
                    case DELETE_REPLICATE:
                        System.out.println("Node "+ membershipView.getNodeIP() + ": Received a DELETE_REPLICATE request");
                        deleteFile(message.contents);
                        saveOwnedKeyList();
                        break;
                    default:
                        break;
                }
            
            }
            if(!membershipStoragePipe.isEmpty()){
                PipeMessage msg=membershipStoragePipe.poll();
                if(msg.type==PipeMessageType.JOIN)
                    handleJoin(msg);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        unicastService.stopUnicastReceiver();
        leaveTransfer();
    }
    
}

    
