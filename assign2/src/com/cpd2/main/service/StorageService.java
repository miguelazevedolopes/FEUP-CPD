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

    MembershipView getResponsibleNodeInfo(StorageMessage message, MembershipLog log) {
        
    }


    @Override
    public void run() {
        unicastService.startUnicastReceiver(membershipService.getMembershipView().getStoragePort());

        while(keepRunning()){
            if(unicastService.getNumberOfObjectsReceived()>0){
                
                StorageMessage message = unicastService.getLastObjectReceived();

                MembershipView nodeInfo= getResponsibleNodeInfo(message,membershipService.getMembershipLogCopy());

                if(nodeInfo.getNodeIP().equals(membershipService.getMembershipView().getNodeIP())){
                    
                }
            }
        }
    }
    
}

    
