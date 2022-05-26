package com.cpd2.main.service;

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

    
