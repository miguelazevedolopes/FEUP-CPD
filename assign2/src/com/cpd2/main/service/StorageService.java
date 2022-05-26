package com.cpd2.main.service;

import java.util.TreeSet;

public class StorageService extends Thread{

    private UnicastService<StorageMessage> unicastService;
    private MembershipService membershipService;
    private TreeSet<String> nodeHashes;
    private boolean stop=false;

    public StorageService(MembershipService membershipService){
        this.nodeHashes = new TreeSet<>();

        // Setting up unicast communication
        this.unicastService = new UnicastService<StorageMessage>();
        this.membershipService=membershipService;
    }

    public void addNodeHash(String nodeHash) {
        nodeHashes.add(nodeHash);
    }

    public void removeNodeHash(String nodeHash) {
        nodeHashes.remove(nodeHash);
    }

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }

    private MembershipView getResponsibleNodeInfo(StorageMessage message, MembershipLog log) {
        String messageHash=Utils.generateHash(message.valueToStore);
        String nodeHash=nodeHashes.higher(messageHash);
        if(nodeHash!=null){
            return log.getNodeInfo(nodeHash);
        }
        else{
            return log.getNodeInfo(nodeHashes.first());
        }
    }


    @Override
    public void run() {
        unicastService.startUnicastReceiver(membershipService.getMembershipView().getStoragePort());

        while(keepRunning()){
            if(unicastService.getNumberOfObjectsReceived()>0){
                StorageMessage message = unicastService.getLastObjectReceived();

                // nao está great o facto de usar membership service assim, rever isso 
                MembershipView nodeInfo= getResponsibleNodeInfo(message,membershipService.getMembershipLogCopy());

                if(nodeInfo.getNodeIP().equals(membershipService.getMembershipView().getNodeIP())){
                    switch (message.type){
                        case PUT:
                            // saveToFile();
                            // replicate();
                            break;
                        case GET:
                            // getFile();
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

    
