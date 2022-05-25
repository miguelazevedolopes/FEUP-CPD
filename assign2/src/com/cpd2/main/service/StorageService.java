package com.cpd2.main.service;

public class StorageService extends Thread{

    private UnicastService<MembershipMessage> unicastService;
    private MembershipService membershipService;
    private boolean stop=false;

    public StorageService(MembershipService membershipService){
        // Setting up unicast communication
        this.unicastService = new UnicastService<MembershipMessage>();
        this.membershipService=membershipService;
    }

    public synchronized void stopService() {
        this.stop = true;
    }

    private synchronized boolean keepRunning() {
        return this.stop == false;
    }


    @Override
    public void run() {
        unicastService.startUnicastReceiver(membershipService.getMembershipView().getStoragePort());

        while(keepRunning()){
            if(unicastService.getNumberOfObjectsReceived()>0){
                
                MembershipMessage message = unicastService.getLastObjectReceived();

                MembershipView nodeInfo= getResponsibleNodeInfo(message,membershipService.getMembershipLogCopy());

                if(nodeInfo.getNodeIP().equals(membershipService.getMembershipView().getNodeIP())){
                    
                }
            }
        }
    }
    
}

    
